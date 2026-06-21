const API_URL = "http://localhost:8080/paciente";

document.getElementById('paciente-form').addEventListener('submit', salvarPaciente);
window.onload = listarTodos;

async function listarTodos() {
    try {
        const response = await fetch(API_URL);
        const pacientes = await response.json();
        renderizarTabela(pacientes);
    } catch (error) { console.error("Erro:", error); }
}

async function salvarPaciente(event) {
    event.preventDefault();
    const id = document.getElementById('paciente-id').value;
    const paciente = {
        nome: document.getElementById('nome').value,
        cpf: document.getElementById('cpf').value,
        dataNascimento: document.getElementById('dataNascimento').value, // Formato yyyy-MM-dd
        telefone: document.getElementById('telefone').value,
        endereco: document.getElementById('endereco').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(paciente)
        });
        if (response.ok) {
            alert("Salvo com sucesso!");
            resetForm();
            listarTodos();
        } else {
            alert("Erro ao salvar (Verifique CPF ou campos vazios)");
        }
    } catch (error) { console.error("Erro:", error); }
}

async function excluirPaciente(id) {
    if(confirm("Excluir paciente?")) {
        await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        listarTodos();
    }
}

function renderizarTabela(pacientes) {
    const tbody = document.getElementById('paciente-table-body');
    tbody.innerHTML = "";
    // Se a API retornar erro ou vazio, pacientes pode não ser array
    if (!Array.isArray(pacientes)) return;

    pacientes.forEach(p => {
        tbody.innerHTML += `
            <tr>
                <td>${p.id}</td>
                <td>${p.nome}</td>
                <td>${p.cpf}</td>
                <td>${p.telefone}</td>
                <td>${p.endereco}</td>
                <td>
                    <div class="actions-container">
                        <button class="btn-edit" onclick="prepararEdicao(${p.id}, '${p.nome}', '${p.cpf}', '${p.dataNascimento}', '${p.telefone}', '${p.endereco}')">Editar</button>
                        <button class="btn-delete" onclick="excluirPaciente(${p.id})">Excluir</button>
                    </div>
                </td>
            </tr>`;
    });
}

function prepararEdicao(id, nome, cpf, data, tel, end) {
    document.getElementById('paciente-id').value = id;
    document.getElementById('nome').value = nome;
    document.getElementById('cpf').value = cpf;
    document.getElementById('dataNascimento').value = data;
    document.getElementById('telefone').value = tel;
    document.getElementById('endereco').value = end;

    document.getElementById('form-title').innerText = "Editar Paciente";
    document.getElementById('btn-cancel').style.display = "inline";
}

function resetForm() {
    document.getElementById('paciente-form').reset();
    document.getElementById('paciente-id').value = "";
    document.getElementById('form-title').innerText = "Cadastrar Paciente";
    document.getElementById('btn-cancel').style.display = "none";
}