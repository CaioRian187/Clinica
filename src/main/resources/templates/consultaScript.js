const API_CONSULTA = "http://localhost:8080/consulta";
const API_MEDICO = "http://localhost:8080/medico";
const API_PACIENTE = "http://localhost:8080/paciente";

document.getElementById('consulta-form').addEventListener('submit', salvarConsulta);

window.onload = async () => {
    await carregarSelects();
    listarConsultas();
};

async function carregarSelects() {
    // 1. Carregar Médicos
    try {
        const resMed = await fetch(API_MEDICO);
        const medicos = await resMed.json();
        const selMed = document.getElementById('select-medico');

        selMed.innerHTML = '<option value="" disabled selected>Selecione um médico...</option>';

        if(Array.isArray(medicos)) {
            medicos.forEach(m => {
                // CORREÇÃO "UNDEFINED" NO MÉDICO:
                // Verifica se existe lista de especialidades e mapeia os nomes
                let nomeEspecialidades = "Clínico Geral"; // Valor padrão
                if (m.especialidades && m.especialidades.length > 0) {
                    nomeEspecialidades = m.especialidades.map(e => e.nome).join(", ");
                }

                selMed.innerHTML += `<option value="${m.id}">${m.nome} - ${nomeEspecialidades}</option>`;
            });
        }
    } catch (e) { console.error("Erro ao carregar médicos:", e); }

    // 2. Carregar Pacientes
    try {
        const resPac = await fetch(API_PACIENTE);
        const pacientes = await resPac.json();
        const selPac = document.getElementById('select-paciente');

        selPac.innerHTML = '<option value="" disabled selected>Selecione um paciente...</option>';

        if(Array.isArray(pacientes)) {
            pacientes.forEach(p => selPac.innerHTML += `<option value="${p.id}">${p.nome}</option>`);
        }
    } catch (e) { console.error("Erro ao carregar pacientes:", e); }
}

async function salvarConsulta(event) {
    event.preventDefault();

    const id = document.getElementById('consulta-id').value;

    // Inputs separados do HTML
    const dataInput = document.getElementById('data').value;
    const horarioInput = document.getElementById('horario').value;
    const medicoId = document.getElementById('select-medico').value;
    const pacienteId = document.getElementById('select-paciente').value;

    if (!dataInput || !horarioInput || !medicoId || !pacienteId) {
        alert("Preencha todos os campos obrigatórios.");
        return;
    }

    // CORREÇÃO DO ERRO AO AGENDAR:
    // O Java espera LocalDateTime ("yyyy-MM-ddTHH:mm:ss")
    const dataHoraFormatada = `${dataInput}T${horarioInput}:00`;

    const consulta = {
        datahora: dataHoraFormatada,
        observacoes: document.getElementById('observacoes').value,
        medicoId: Number(medicoId),
        pacienteId: Number(pacienteId)
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_CONSULTA}/${id}` : API_CONSULTA;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(consulta)
        });

        if (response.ok) {
            alert("Consulta agendada com sucesso!");
            resetForm();
            listarConsultas();
        } else {
            const erroText = await response.text();
            console.error("Erro do servidor:", erroText);
            alert("Erro ao agendar. Verifique o console para detalhes.");
        }
    } catch (error) {
        console.error(error);
        alert("Erro de conexão com o servidor.");
    }
}

async function listarConsultas() {
    try {
        const response = await fetch(API_CONSULTA);
        const consultas = await response.json();
        const tbody = document.getElementById('consulta-table-body');
        tbody.innerHTML = "";

        if (!Array.isArray(consultas)) return;

        consultas.forEach(c => {
            const nomeMedico = c.nomeMedico || 'N/A';
            const nomePaciente = c.nomePaciente || 'N/A';

            // FORMATAR PARA BRASIL (DD/MM/YYYY HH:MM)
            let dataExibicao = "Data Inválida";
            if (c.datahora) {
                const dataObj = new Date(c.datahora);
                dataExibicao = dataObj.toLocaleDateString('pt-BR') + ' às ' +
                               dataObj.toLocaleTimeString('pt-BR', {hour: '2-digit', minute:'2-digit'});
            }

            tbody.innerHTML += `
                <tr>
                    <td>${dataExibicao}</td>
                    <td>${nomeMedico}</td>
                    <td>${nomePaciente}</td>
                    <td>${c.observacoes}</td>
                    <td>
                        <div class="actions-container">
                            <button class="btn-delete" onclick="excluirConsulta(${c.id})">Cancelar</button>
                        </div>
                    </td>
                </tr>`;
        });
    } catch (error) { console.error("Erro ao listar consultas:", error); }
}

async function excluirConsulta(id) {
    if(confirm("Cancelar esta consulta?")) {
        await fetch(`${API_CONSULTA}/${id}`, { method: 'DELETE' });
        listarConsultas();
    }
}

function resetForm() {
    document.getElementById('consulta-form').reset();
    document.getElementById('consulta-id').value = "";
}