const API_URL = "http://localhost:8080/especialidade";

document.getElementById('especialidade-form').addEventListener('submit', salvarEspecialidade);
window.onload = listarTodos;

async function listarTodos() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Erro ao buscar dados");
        const especialidades = await response.json();
        renderizarTabela(especialidades);
    } catch (error) {
        console.error("Erro ao listar:", error);
        alert("Não foi possível carregar a lista de especialidades.");
    }
}

async function buscarPorNome() {
    const nome = document.getElementById('search-name').value;
    if (!nome) return listarTodos();

    try {
        const response = await fetch(`${API_URL}/nome/${nome}`);
        if (response.ok) {
            const especialidade = await response.json();
            renderizarTabela([especialidade]);
        } else {
            alert("Especialidade não encontrada.");
        }
    } catch (error) {
        console.error("Erro na busca:", error);
    }
}

async function salvarEspecialidade(event) {
    event.preventDefault();

    const id = document.getElementById('especialidade-id').value;
    const especialidadeData = {
        nome: document.getElementById('nome').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(especialidadeData)
        });

        if (response.ok) {
            alert(id ? "Especialidade atualizada!" : "Especialidade cadastrada!");
            resetForm();
            listarTodos();
        }
    } catch (error) {
        console.error("Erro ao salvar:", error);
    }
}

async function excluirEspecialidade(id) {
    if (!confirm("Deseja realmente excluir esta especialidade?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        if (response.ok) {
            listarTodos();
        }
    } catch (error) {
        console.error("Erro ao excluir:", error);
    }
}

function renderizarTabela(especialidades) {
    const tbody = document.getElementById('especialidade-table-body');
    tbody.innerHTML = "";

    if (!Array.isArray(especialidades)) return;

    especialidades.forEach(e => {
        tbody.innerHTML += `
            <tr>
                <td>${e.id}</td>
                <td>${e.nome}</td>
                <td>
                    <div class="actions-container">
                        <button class="btn-edit" onclick="prepararEdicao('${e.id}', '${e.nome}')">Editar</button>
                        <button class="btn-delete" onclick="excluirEspecialidade('${e.id}')">Excluir</button>
                    </div>
                </td>
            </tr>
        `;
    });
}

function prepararEdicao(id, nome) {
    document.getElementById('especialidade-id').value = id;
    document.getElementById('nome').value = nome;

    document.getElementById('form-title').innerText = "Editar Especialidade";
    document.getElementById('btn-cancel').style.display = "inline";
}

function resetForm() {
    document.getElementById('especialidade-form').reset();
    document.getElementById('especialidade-id').value = "";
    document.getElementById('form-title').innerText = "Cadastrar Especialidade";
    document.getElementById('btn-cancel').style.display = "none";
}