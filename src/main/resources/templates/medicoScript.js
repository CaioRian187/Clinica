const API_URL = "http://localhost:8080/medico";
const API_ESPECIALIDADE = "http://localhost:8080/especialidade";

document.getElementById('medico-form').addEventListener('submit', salvarMedico);

// Carrega as especialidades e depois lista os médicos
window.onload = async () => {
    await carregarEspecialidades();
    listarTodos();
};

// Busca as especialidades no BD e coloca no <select>
async function carregarEspecialidades() {
    try {
        const response = await fetch(API_ESPECIALIDADE);
        const lista = await response.json();
        const select = document.getElementById('select-especialidade');

        select.innerHTML = '<option value="" disabled selected>Selecione uma especialidade...</option>';

        if (Array.isArray(lista)) {
            lista.forEach(esp => {
                select.innerHTML += `<option value="${esp.id}">${esp.nome}</option>`;
            });
        }
    } catch (error) {
        console.error("Erro ao carregar especialidades:", error);
    }
}

async function listarTodos() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Erro ao buscar dados");
        const medicos = await response.json();
        renderizarTabela(medicos);
    } catch (error) {
        console.error("Erro:", error);
    }
}

async function salvarMedico(event) {
    event.preventDefault();

    const id = document.getElementById('medico-id').value;
    const especialidadeId = document.getElementById('select-especialidade').value;

    const medicoData = {
        nome: document.getElementById('nome').value,
        crm: document.getElementById('crm').value,
        telefone: document.getElementById('telefone').value,
        especialidades: [
            { id: especialidadeId }
        ]
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(medicoData)
        });

        if (response.ok) {
            alert(id ? "Médico atualizado!" : "Médico cadastrado!");
            resetForm();
            listarTodos();
        } else {
            alert("Erro ao salvar. Verifique se todos os campos estão preenchidos.");
        }
    } catch (error) {
        console.error("Erro ao salvar:", error);
    }
}

async function excluirMedico(id) {
    if (!confirm("Deseja realmente excluir este médico?")) return;
    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        if (response.ok) listarTodos();
        else alert("Não foi possível excluir (Médico possui consultas)");
    } catch (error) {
        console.error("Erro:", error);
    }
}

function renderizarTabela(medicos) {
    const tbody = document.getElementById('medico-table-body');
    tbody.innerHTML = "";

    if (!Array.isArray(medicos)) return;

    medicos.forEach(m => {
        // Pega os nomes das especialidades e junta com vírgula
        let nomesEspecialidades = "Nenhuma";
        if(m.especialidades && m.especialidades.length > 0){
            nomesEspecialidades = m.especialidades.map(e => e.nome).join(", ");
        }

        tbody.innerHTML += `
            <tr>
                <td>${m.id}</td>
                <td>${m.nome}</td>
                <td>${m.crm}</td>
                <td>${m.telefone}</td>
                <td>${nomesEspecialidades}</td>
                <td>
                    <div class="actions-container">
                        <button class="btn-edit" onclick="prepararEdicao(${m.id}, '${m.nome}', '${m.crm}', '${m.telefone}')">Editar</button>
                        <button class="btn-delete" onclick="excluirMedico(${m.id})">Excluir</button>
                    </div>
                </td>
            </tr>
        `;
    });
}

function prepararEdicao(id, nome, crm, telefone) {
    document.getElementById('medico-id').value = id;
    document.getElementById('nome').value = nome;
    document.getElementById('crm').value = crm;
    document.getElementById('telefone').value = telefone;

    document.getElementById('form-title').innerText = "Editar Médico";
    document.getElementById('btn-cancel').style.display = "inline";
}

function resetForm() {
    document.getElementById('medico-form').reset();
    document.getElementById('medico-id').value = "";
    document.getElementById('form-title').innerText = "Cadastrar Médico";
    document.getElementById('btn-cancel').style.display = "none";
}