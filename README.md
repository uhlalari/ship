🧾 Documentação Técnica – Projeto Game Ship (Android)
📌 Resumo da Arquitetura
Este projeto Android implementa um jogo estilo “nave atiradora”, estruturado com base em Clean Architecture e princípios de Clean Code.

Camadas separadas por responsabilidade:

presentation: interação com UI e ViewModel

domain: regras de negócio e modelos de entidades

data: implementação de repositórios

Padrões aplicados:

MVVM (Model-View-ViewModel)

Single Responsibility Principle (SRP)

Separation of Concerns (SoC)

⚙️ Tecnologias e Ferramentas
Linguagem: Kotlin

Android Architecture Components: ViewModel, LiveData

Threading: Handler para game loop

Layout: FrameLayout, ImageView, ConstraintLayout

🗂️ Classes e Responsabilidades
🧠 GameViewModel
Pacote: com.game.ship.presentation.viewmodel

Responsabilidade: Controla a lógica do jogo e o estado da interface gráfica.

Funções principais:

Movimentação da nave com interpolação suave (lerp)

Disparo de balas

Atualização de colisões e entidades

Gerenciamento de game over

Reinício do jogo

Observável: LiveData<GameState>

📱 GameActivity
Pacote: com.game.ship.presentation.view

Responsabilidade: Tela principal do jogo.

Funções:

Renderiza nave, inimigos, tiros e placar

Captura eventos de toque para mover a nave e atirar

Inicia e controla o game loop (Handler)

Reinicializa o jogo ao clicar no botão de restart

Integração com ViewModel

🧩 GameState
Pacote: com.game.ship.presentation.state

Responsabilidade: Representa o estado atual do jogo.

Campos:

Nave, balas, inimigos, pontuação, e estado de fim de jogo (isGameOver)

🧪 ShootBulletUseCase
Pacote: com.game.ship.domain.usecase

Responsabilidade: Cria uma nova bala baseada na posição da nave.

Aplicação de Clean Code: encapsula regra de criação do tiro, separado da UI.

🧪 MoveShipUseCase
Pacote: com.game.ship.domain.usecase

Responsabilidade: Move a nave com base na coordenada de toque.

Ponto chave: desloca o x para manter a nave centralizada.

🧪 GenerateEnemyUseCase
Pacote: com.game.ship.domain.usecase

Responsabilidade: Gera um inimigo em uma posição aleatória da tela.

Utiliza: GameRepository

🧪 UpdateGameUseCase
Pacote: com.game.ship.domain.usecase

Responsabilidade: Atualiza balas, inimigos e detecta colisões ou game over.

Funções:

updateBullets()

updateEnemies()

detectCollisions()

hasEnemyReachedShip()

📦 GameRepositoryImpl
Pacote: com.game.ship.data.repositoryimpl

Responsabilidade: Implementa GameRepository para gerar inimigos.

Tecnologia usada: Kotlin Random

🧾 GameRepository
Pacote: com.game.ship.domain.repository

Responsabilidade: Abstração da fonte de dados do jogo (ex: inimigos).

📦 Modelos de Domínio
Ship, Enemy, Bullet
Pacote: com.game.ship.domain.model

Responsabilidade: Representam as entidades principais do jogo.

Design: Simples data class com atributos essenciais.

🏗️ Boas práticas utilizadas
Clean Architecture: separação clara entre apresentação, domínio e dados

Single Responsibility: cada classe tem apenas uma responsabilidade

Inversão de dependência: uso de interface (GameRepository)

Imutabilidade: uso de copy() para atualizar estado

Observabilidade: LiveData para comunicação unidirecional com a view
