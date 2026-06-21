const API_EXAME = "http://localhost:8080/exame";
const API_MEDICO = "http://localhost:8080/medico";
const API_PACIENTE = "http://localhost:8080/paciente";

document.getElementById('exame-form').addEventListener('submit', salvarExame);

window.onload = async () => {
    await carregarSelects();
    listarExames();
};

async function carregarSelects() {
    // Carregar Médicos
    const resMed = await fetch(API_MEDICO);
    const medicos = await resMed.json();
    const selMed = document.getElementById('select-medico');
    medicos.forEach(m => selMed.innerHTML += `<option value="${m.id}">${m.nome}</option>`);

    // Carregar Pacientes
    const resPac = await fetch(API_PACIENTE);
    const pacientes = await resPac.json();
    const selPac = document.getElementById('select-paciente');
    pacientes.forEach(p => selPac.innerHTML += `<option value="${p.id}">${p.nome}</option>`);
}

async function listarExames() {
    try {
        const response = await fetch(API_EXAME);
        const exames = await response.json();
        const tbody = document.getElementById('exame-table-body');
        tbody.innerHTML = "";

        if (!Array.isArray(exames)) return;

        exames.forEach(e => {
            // FORMATAR PARA BRASIL (DD/MM/YYYY HH:MM)
            let dataExibicao = "Data Inválida";
            if (e.dataHora) {
                const dataObj = new Date(e.dataHora);
                dataExibicao = dataObj.toLocaleDateString('pt-BR') + ' às ' +
                               dataObj.toLocaleTimeString('pt-BR', {hour: '2-digit', minute:'2-digit'});
            }

            tbody.innerHTML += `
                <tr>
                    <td>${e.nome}</td>
                    <td>${dataExibicao}</td>
                    <td>${e.nomeMedico || '-'}</td>
                    <td>${e.nomePaciente || '-'}</td>
                    <td>
                        <div class="actions-container">
                            <button class="btn-delete" onclick="excluirExame(${e.id})">Excluir</button>
                        </div>
                    </td>
                </tr>`;
        });
    } catch (error) { console.error(error); }
}

async function salvarExame(event) {
    event.preventDefault();
    const id = document.getElementById('exame-id').value;

    const dataInput = document.getElementById('data').value;
    const horarioInput = document.getElementById('horario').value;

    if (!dataInput || !horarioInput) {
        alert("Preencha a data e o horário.");
        return;
    }

    const dataHoraFormatada = `${dataInput}T${horarioInput}:00`;

    const exame = {
        nome: document.getElementById('nome').value,
        dataHora: dataHoraFormatada,
        descricao: document.getElementById('descricao').value,
        medicoId: Number(document.getElementById('select-medico').value),
        pacienteId: Number(document.getElementById('select-paciente').value)
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_EXAME}/${id}` : API_EXAME;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(exame)
        });
        if (response.ok) {
            alert("Exame salvo!");
            resetForm();
            listarExames();
        }
    } catch (error) { console.error(error); }
}

async function excluirExame(id) {
    if(confirm("Excluir este exame?")) {
        await fetch(`${API_EXAME}/${id}`, { method: 'DELETE' });
        listarExames();
    }
}

function resetForm() {
    document.getElementById('exame-form').reset();
    document.getElementById('exame-id').value = "";
}