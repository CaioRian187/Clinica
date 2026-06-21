const API_RECEITA = "http://localhost:8080/receita";
const API_CONSULTA = "http://localhost:8080/consulta";

document.getElementById('receita-form').addEventListener('submit', salvarReceita);

window.onload = async () => {
    await carregarConsultas();
    listarReceitas();
};

async function carregarConsultas() {
    try {
        const response = await fetch(API_CONSULTA);
        const consultas = await response.json();
        const select = document.getElementById('select-consulta');
        select.innerHTML = '<option value="" disabled selected>Selecione uma consulta...</option>';

        if(Array.isArray(consultas)){
            consultas.forEach(c => {
                const pacNome = c.nomePaciente || 'Sem Paciente';
                let dataFormatada = "Data n/d";
                if(c.datahora) {
                    const dataObj = new Date(c.datahora);
                    dataFormatada = dataObj.toLocaleDateString('pt-BR') + " " + dataObj.toLocaleTimeString('pt-BR', {hour: '2-digit', minute:'2-digit'});
                }
                select.innerHTML += `<option value="${c.id}">${dataFormatada} - ${pacNome}</option>`;
            });
        }
    } catch (e) { console.error("Erro ao carregar consultas:", e); }
}

async function listarReceitas() {
    try {
        const response = await fetch(API_RECEITA);
        const receitas = await response.json();
        const tbody = document.getElementById('receita-table-body');
        tbody.innerHTML = "";

        if (!Array.isArray(receitas)) return;

        receitas.forEach(r => {
            // Nome do paciente vem direto do DTO flat
            const nomePaciente = r.nomePaciente || 'N/A';

            // Formatar data de emissão para BR (DD/MM/YYYY HH:MM)
            let dataEmissaoStr = '-';
            if(r.dataEmissao) {
                const dataObj = new Date(r.dataEmissao);
                dataEmissaoStr = dataObj.toLocaleDateString('pt-BR') + ' às ' +
                                 dataObj.toLocaleTimeString('pt-BR', {hour: '2-digit', minute:'2-digit'});
            }

            tbody.innerHTML += `
                <tr>
                    <td>${r.receitaId}</td>
                    <td>${dataEmissaoStr}</td>
                    <td>${r.medicamento || '-'}</td>
                    <td>${r.instrucoes || '-'}</td>
                    <td>${nomePaciente}</td>
                    <td>
                        <div class="actions-container">
                            <button class="btn-delete" onclick="excluirReceita(${r.receitaId})">Excluir</button>
                        </div>
                    </td>
                </tr>`;
        });
    } catch (error) { console.error("Erro ao listar receitas:", error); }
}

async function salvarReceita(event) {
    event.preventDefault();
    const id = document.getElementById('receita-id').value;
    const consultaId = document.getElementById('select-consulta').value;

    if(!consultaId) { alert("Selecione uma consulta."); return; }

    // O ReceitaRequestDTO espera: dataEmissao (LocalDateTime), medicamento, dosagem, instrucoes, consultaId (Long)
    // dataEmissao é gerada automaticamente como a data/hora atual + 1 min para evitar rejeição do @FutureOrPresent
    const agora = new Date();
    agora.setMinutes(agora.getMinutes() + 1);
    const dataEmissaoFormatada = agora.getFullYear() + '-' +
        String(agora.getMonth() + 1).padStart(2, '0') + '-' +
        String(agora.getDate()).padStart(2, '0') + 'T' +
        String(agora.getHours()).padStart(2, '0') + ':' +
        String(agora.getMinutes()).padStart(2, '0') + ':' +
        String(agora.getSeconds()).padStart(2, '0');

    const receita = {
        dataEmissao: dataEmissaoFormatada,
        medicamento: document.getElementById('medicamento').value,
        dosagem: document.getElementById('dosagem').value,
        instrucoes: document.getElementById('instrucoes').value,
        consultaId: Number(consultaId)
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_RECEITA}/${id}` : API_RECEITA;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(receita)
        });

        if (response.ok) {
            alert("Receita salva com sucesso!");
            resetForm();
            listarReceitas();
        } else {
            const erroText = await response.text();
            console.error("Erro do servidor:", erroText);
            alert("Erro ao salvar receita. Verifique o console para detalhes.");
        }
    } catch (error) {
        console.error(error);
        alert("Erro de conexão com o servidor.");
    }
}

async function excluirReceita(id) {
    if(confirm("Excluir esta receita?")) {
        await fetch(`${API_RECEITA}/${id}`, { method: 'DELETE' });
        listarReceitas();
    }
}

function resetForm() {
    document.getElementById('receita-form').reset();
    document.getElementById('receita-id').value = "";
}