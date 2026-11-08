# ASCII Ray Tracing — Java

## Sobre o projeto

Este projeto implementa uma pequena demonstração de **Ray Tracing (rastreamento de raios)** utilizando Java, com toda a renderização realizada diretamente no terminal.

A cena apresenta uma esfera em movimento, uma fonte de luz e um plano que recebe sombras. Enquanto a esfera quica sob o efeito da gravidade, a câmera gira ao redor da cena, proporcionando uma visualização tridimensional animada.

A imagem é construída utilizando caracteres Unicode de diferentes intensidades, simulando sombreamento e iluminação sem a necessidade de uma interface gráfica.

## Demonstração

- Renderização tridimensional diretamente no terminal.
- Esfera com movimento e colisão com o plano.
- Simulação de gravidade.
- Iluminação e sombreamento.
- Efeito de luz (*Bloom*).
- Câmera com rotação ao redor da cena.
- Renderização utilizando diferentes níveis de intensidade dos caracteres.

## Tecnologias utilizadas

- **Java 16+:** linguagem utilizada no desenvolvimento.
- **Java Math:** operações matemáticas e cálculos geométricos.
- **Ray Tracing:** técnica utilizada para calcular interseções e iluminação.
- **Terminal UTF-8:** exibição dos caracteres utilizados na renderização.
- **ANSI Escape Codes:** atualização da imagem diretamente no terminal.

## Estrutura do projeto

```text
rayrtracing-poo/
│
├── raytrace/
│   ├── main.java    # Lógica principal e renderização
│   ├── vec3.java    # Operações matemáticas com vetores 3D
│   └── ray.java     # Estrutura e representação de raios
│
├── README.md
└── make.bat         # Script de compilação e execução (opcional)
```

### Organização dos componentes

| Arquivo | Responsabilidade |
|---|---|
| `main.java` | Gerenciamento da cena, iluminação, interseções, animação e renderização. |
| `vec3.java` | Implementação de vetores tridimensionais e operações matemáticas. |
| `ray.java` | Representação de um raio por meio de sua origem e direção. |

## Requisitos

Antes de executar o projeto, certifique-se de possuir:

- [Java JDK 16 ou superior](https://www.oracle.com/java/technologies/downloads/).
- Windows Terminal ou PowerShell.
- Terminal com suporte a UTF-8 e sequências ANSI.
- Janela de terminal com pelo menos **120 colunas por 30 linhas**.

Para verificar a instalação do Java, execute:

```bash
java -version
javac -version
```

## Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/cauakssz/rayrtracing-poo.git
```

### 2. Entre na pasta do projeto

```bash
cd rayrtracing-poo
```

### 3. Compile os arquivos

```bash
javac -encoding UTF-8 -d . raytrace/main.java raytrace/vec3.java raytrace/ray.java
```

Se nenhum erro for apresentado, a compilação foi concluída com sucesso.

### 4. Configure a codificação do terminal

No PowerShell, execute:

```powershell
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
```

### 5. Execute o programa

```bash
java -cp . raytrace.Main
```

**Importante:** execute os comandos a partir da pasta raiz do projeto, e não de dentro da pasta `raytrace`.

## Execução simplificada no Windows

Também é possível utilizar o arquivo `make.bat`, caso esteja presente na raiz do projeto.

Basta dar dois cliques no arquivo para iniciar o processo de compilação e execução.

## Detalhes da implementação

### Renderização

A imagem é construída por meio de uma matriz de caracteres, na qual cada caractere representa um determinado nível de intensidade luminosa.

São utilizados caracteres Unicode de sombreamento:

| Caractere | Intensidade |
|---|---|
| Espaço | Ausência de iluminação |
| ░ | Baixa |
| ▒ | Média |
| ▓ | Alta |
| █ | Máxima |

### Iluminação e sombras

O projeto calcula a interação dos raios com os elementos da cena, considerando:

- Interseção entre raios e esfera.
- Interseção entre raios e plano.
- Iluminação direta.
- Sombras projetadas pela esfera.
- Efeito de brilho (*Bloom*) próximo à fonte de luz.

### Animação

A movimentação da cena é realizada por atualizações sucessivas de posição e velocidade.

A esfera sofre a influência da gravidade e inverte sua velocidade vertical ao atingir o plano. Simultaneamente, a câmera realiza uma rotação em torno da cena.

A animação possui duração aproximada de **30 segundos**, com atualização planejada em intervalos de aproximadamente 33 milissegundos.

## Solução de problemas

| Problema | Possível solução |
|---|---|
| `javac is not recognized` | Instale o JDK e configure a variável PATH. |
| `Could not find or load main class` | Execute o programa na pasta raiz e utilize `raytrace.Main`. |
| Caracteres aparecem incorretamente | Configure a saída do terminal para UTF-8. |
| Imagem aparece desorganizada | Maximize o terminal e verifique se ele possui pelo menos 120 × 30 caracteres. |
| Animação não é exibida corretamente | Utilize Windows Terminal ou PowerShell com suporte a sequências ANSI. |

Para interromper a execução antes do término, pressione `Ctrl + C`.

## Objetivo acadêmico

Este projeto tem como objetivo explorar conceitos de computação gráfica e programação orientada a objetos utilizando Java.

Durante sua implementação, são trabalhados conceitos como:

- Vetores e operações matemáticas tridimensionais.
- Geometria analítica.
- Rastreamento de raios.
- Interseções geométricas.
- Iluminação e sombreamento.
- Simulação física básica.
- Animação e renderização no terminal.
---
