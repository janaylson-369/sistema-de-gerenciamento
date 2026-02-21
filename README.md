# 🎟️ Sistema de Gestão de Ingressos

Este é um sistema desktop desenvolvido em **Java** para a gestão e venda de ingressos de partidas de futebol. O projeto foca na aplicação de padrões de projeto para persistência de dados e uma interface de usuário fluida.

## 🚀 Funcionalidades

### 👤 Perfil Torcedor
* **Acesso Simplificado**: Cadastro e login automático através de Nome, E-mail e CPF.
* **Vitrine de Jogos**: Visualização dinâmica de partidas disponíveis com informações de estádio, data e preço mínimo.
* **Compra de Bilhetes**: Sistema de reserva de assentos com atualização de status em tempo real no banco de dados.
* **Histórico Pessoal**: Visualização detalhada de todos os ingressos comprados em formato de "bilhete".

### 🛠️ Perfil Administrador
* **Gestão de Partidas**: Cadastro centralizado de Estádios, Times e Jogos.
* **Geração de Ingressos em Lote**: Criação automática de centenas de ingressos com base na capacidade do estádio através de *batch processing* para performance.
* **Edição e Atualização**: Modificação de horários, preços e dados de estádio para partidas futuras.
* **Controle de Segurança**: Bloqueio de exclusão para jogos que já possuem ingressos vendidos.



[Image of Model-View-Controller architecture diagram]


## 🏗️ Arquitetura do Projeto

O sistema foi construído utilizando o padrão **MVC (Model-View-Controller)** para garantir a separação de responsabilidades:

1.  **Model**: Classes POJO que representam as entidades do negócio.
2.  **View**: Interfaces declarativas desenvolvidas em **FXML (JavaFX)**.
3.  **Controller**: Lógica de controle que faz a ponte entre a interface e os dados.
4.  **DAO (Data Access Object)**: Camada de persistência que isola todo o código SQL da lógica da aplicação.

## 🛠️ Tecnologias Utilizadas

* **Linguagem**: Java.
* **Interface Gráfica**: JavaFX / FXML.
* **Banco de Dados**: PostgreSQL.
* **Persistência**: JDBC com gestão de transações (Commit/Rollback).

## 🗄️ Estrutura da Base de Dados

A base de dados foi projetada para garantir a integridade referencial com o seguinte esquema:

* `Estadio`: Registro de locais e capacidades.
* `Torcedor`: Dados únicos de usuários.
* `Times`: Registro de clubes (garante nomes únicos no sistema).
* `Jogo`: Entidade central que vincula data, local e equipes.
* `Ingresso`: Controle individual de assentos e status de venda.

---

### 📝 Como executar
1. Execute o `script.sql` no seu servidor PostgreSQL.
2. Configure as credenciais de acesso na sua classe de conexão `ConnectionPostgres`.
3. Compile e execute o projeto através da classe `App.java`.
