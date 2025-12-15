-- Script de inicialização do banco de dados da Clínica
-- Insere dados iniciais de médicos, sintomas e doenças

-- Inserir Médicos
INSERT INTO medicos (nome, crm, especialidade, email, telefone, data_cadastro, data_atualizacao) VALUES
('Dr. João Silva', 'CRM-SP-123456', 'Cardiologista', 'joao.silva@hospital.com', '(11) 98765-4321', NOW(), NOW()),
('Dra. Maria Santos', 'CRM-SP-234567', 'Pediatra', 'maria.santos@hospital.com', '(11) 98765-4322', NOW(), NOW()),
('Dr. Pedro Costa', 'CRM-SP-345678', 'Ortopedista', 'pedro.costa@hospital.com', '(11) 98765-4323', NOW(), NOW()),
('Dra. Ana Lima', 'CRM-SP-456789', 'Neurologista', 'ana.lima@hospital.com', '(11) 98765-4324', NOW(), NOW()),
('Dr. Carlos Mendes', 'CRM-SP-567890', 'Clínico Geral', 'carlos.mendes@hospital.com', '(11) 98765-4325', NOW(), NOW());

-- Inserir Sintomas
INSERT INTO sintomas (nome, prioridade) VALUES
('febre', 'PADRAO'),
('tosse', 'PADRAO'),
('cansaço', 'PADRAO'),
('dor de cabeça', 'PADRAO'),
('náusea', 'PADRAO'),
('dor na perna', 'ALTA'),
('sangramento agudo', 'EMERGENCIAL'),
('dores internas', 'EMERGENCIAL'),
('falta de ar', 'ALTA'),
('tontura', 'ALTA'),
('vômito', 'ALTA'),
('dor no peito', 'EMERGENCIAL');

-- Inserir Doenças
INSERT INTO doencas (nome, descricao, tratamento_padrao, requer_exame_alta_complexidade, tipo_exame_sugerido) VALUES
('COVID-19', 'Infecção viral respiratória', 'Isolamento, hidratação, antitérmicos', false, null),
('Gripe', 'Infecção viral respiratória comum', 'Repouso, hidratação, antitérmicos', false, null),
('Enxaqueca', 'Dor de cabeça intensa', 'Analgésicos, repouso em ambiente escuro', false, null),
('Fratura', 'Quebra óssea', 'Imobilização, possível cirurgia', true, 'Raio-X'),
('Hemorragia interna', 'Sangramento interno', 'EMERGÊNCIA - Intervenção cirúrgica', true, 'Tomografia'),
('Gastroenterite', 'Infecção gastrointestinal', 'Hidratação, dieta leve, antieméticos', false, null);

-- Associar Sintomas com Doenças
-- COVID-19: febre, tosse, cansaço
INSERT INTO doenca_sintoma (doenca_id, sintoma_id) 
SELECT d.id, s.id FROM doencas d, sintomas s 
WHERE d.nome = 'COVID-19' AND s.nome IN ('febre', 'tosse', 'cansaço');

-- Gripe: febre, tosse
INSERT INTO doenca_sintoma (doenca_id, sintoma_id) 
SELECT d.id, s.id FROM doencas d, sintomas s 
WHERE d.nome = 'Gripe' AND s.nome IN ('febre', 'tosse');

-- Enxaqueca: dor de cabeça
INSERT INTO doenca_sintoma (doenca_id, sintoma_id) 
SELECT d.id, s.id FROM doencas d, sintomas s 
WHERE d.nome = 'Enxaqueca' AND s.nome = 'dor de cabeça';

-- Fratura: dor na perna
INSERT INTO doenca_sintoma (doenca_id, sintoma_id) 
SELECT d.id, s.id FROM doencas d, sintomas s 
WHERE d.nome = 'Fratura' AND s.nome = 'dor na perna';

-- Hemorragia interna: sangramento agudo, dores internas
INSERT INTO doenca_sintoma (doenca_id, sintoma_id) 
SELECT d.id, s.id FROM doencas d, sintomas s 
WHERE d.nome = 'Hemorragia interna' AND s.nome IN ('sangramento agudo', 'dores internas');

-- Gastroenterite: náusea, vômito
INSERT INTO doenca_sintoma (doenca_id, sintoma_id) 
SELECT d.id, s.id FROM doencas d, sintomas s 
WHERE d.nome = 'Gastroenterite' AND s.nome IN ('náusea', 'vômito');
