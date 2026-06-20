const API_URL = "http://localhost:8080/medico";
const API_ESPECIALIDADE = "http://localhost:8080/especialidade";

document.getElementById('medico-form').addEventListener('submit', salvarMedico);

// Carrega as especialidades e depois lista os médicos
window.onload = async () => {
    await carregarEspecialidades();
    listarTodos();
};

// Busca as especialidades no BD e coloca no container de checkboxes
async function carregarEspecialidades() {
    try {
        const response = await fetch(API_ESPECIALIDADE);
        const lista = await response.json();
        const container = document.getElementById('especialidades-container');

        container.innerHTML = '';

        if (Array.isArray(lista)) {
            lista.forEach(esp => {
                container.innerHTML += `
                    <label class="checkbox-item">
                        <input type="checkbox" name="especialidades" value="${esp.id}" id="esp-${esp.id}">
                        <span>${esp.nome}</span>
                    </label>
                `;
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
    const checkedCheckboxes = document.querySelectorAll('input[name="especialidades"]:checked');
    const listEspecialidadesIds = Array.from(checkedCheckboxes).map(cb => Number(cb.value));

    if (listEspecialidadesIds.length === 0) {
        alert("Selecione pelo menos uma especialidade!");
        return;
    }

    const medicoData = {
        nome: document.getElementById('nome').value,
        crm: document.getElementById('crm').value,
        telefone: document.getElementById('telefone').value,
        listEspecialidadesIds: listEspecialidadesIds
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
                        <button class="btn-edit" onclick="prepararEdicao(${m.id}, '${m.nome}', '${m.crm}', '${m.telefone}', '${(m.especialidades || []).map(e => e.id).join(',')}')">Editar</button>
                        <button class="btn-delete" onclick="excluirMedico(${m.id})">Excluir</button>
                    </div>
                </td>
            </tr>
        `;
    });
}

function prepararEdicao(id, nome, crm, telefone, especialidadesIdsStr) {
    document.getElementById('medico-id').value = id;
    document.getElementById('nome').value = nome;
    document.getElementById('crm').value = crm;
    document.getElementById('telefone').value = telefone;

    // Desmarca todos os checkboxes antes de marcar os do médico editado
    const checkboxes = document.querySelectorAll('input[name="especialidades"]');
    checkboxes.forEach(cb => cb.checked = false);

    // Marca as especialidades associadas ao médico
    if (especialidadesIdsStr) {
        const ids = especialidadesIdsStr.split(',');
        ids.forEach(espId => {
            const cb = document.getElementById('esp-' + espId);
            if (cb) cb.checked = true;
        });
    }

    document.getElementById('form-title').innerText = "Editar Médico";
    document.getElementById('btn-cancel').style.display = "inline";
}

function resetForm() {
    document.getElementById('medico-form').reset();
    document.getElementById('medico-id').value = "";
    
    // Desmarca todos os checkboxes
    const checkboxes = document.querySelectorAll('input[name="especialidades"]');
    checkboxes.forEach(cb => cb.checked = false);

    document.getElementById('form-title').innerText = "Cadastrar Médico";
    document.getElementById('btn-cancel').style.display = "none";
}