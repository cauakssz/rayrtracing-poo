# ASCII Ray Tracing — Java

## Sobre o projeto

Este projeto implementa uma pequena demonstração de **Ray Tracing (rastreamento de raios)** utilizando Java, com toda a renderização realizada diretamente no terminal.

A cena apresenta uma esfera em movimento, uma fonte de luz e um plano que recebe sombras. Enquanto a esfera quica sob o efeito da gravidade, a câmera gira ao redor da cena, proporcionando uma visualização tridimensional animada.

A imagem é construída utilizando caracteres Unicode de diferentes intensidades, simulando sombreamento e iluminação sem a necessidade de uma interface gráfica.

O código foi organizado seguindo os princípios da **Programação Orientada a Objetos (POO)**: cada elemento da cena (luz, esfera, plano e câmera) é representado por uma classe com seus próprios dados e comportamentos.

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
- **Programação Orientada a Objetos:** classes, encapsulamento, imutabilidade, interfaces e polimorfismo.
- **Java Math:** operações matemáticas e cálculos geométricos.
- **Ray Tracing:** técnica utilizada para calcular interseções e iluminação.
- **Terminal UTF-8:** exibição dos caracteres utilizados na renderização.
- **ANSI Escape Codes:** atualização da imagem diretamente no terminal.

## Estrutura do projeto

```
rayrtracing-poo/
│
├── raytrace/
│   ├── main.java    # Cena, classes dos objetos, iluminação e renderização
│   ├── vec3.java    # Vetor 3D imutável e suas operações matemáticas
│   └── ray.java     # Raio imutável (origem e direção)
│
├── README.md
└── make.bat         # Script de compilação e execução (opcional)
```

### Organização dos componentes

| Arquivo     | Responsabilidade                                                                                                                         |
|-------------|------------------------------------------------------------------------------------------------------------------------------------------|
| `main.java` | Contém a interface `Surface` e as classes `Light`, `Sphere`, `Plane` e `Camera`, além do cálculo de brilho, da renderização e da animação. |
| `vec3.java` | Implementação de vetores tridimensionais imutáveis e de suas operações matemáticas.                                                      |
| `ray.java`  | Representação imutável de um raio por meio de sua origem e direção.                                                                      |

### Classes dentro do `main.java`

| Classe / Interface | Papel                                                                                          |
|--------------------|------------------------------------------------------------------------------------------------|
| `Surface`          | Interface: define que toda superfície sabe calcular a interseção com um raio (`intersect`).    |
| `Sphere`           | Esfera que implementa `Surface`. Guarda centro, velocidade e raio, e simula gravidade e quique. |
| `Plane`            | Plano (chão) que implementa `Surface`. Também gera o raio de sombra em direção à luz.          |
| `Light`            | Fonte de luz. Sabe se um raio a atinge (`intersects`) e calcula o brilho ao redor (`bloom`).   |
| `Camera`           | Guarda posição e direção do olhar, monta o raio de cada pixel (`viewRay`) e gira (`rotateY`).  |
| `Main`             | Cria os objetos, calcula o brilho de cada pixel (`shade`), desenha a tela e controla o loop.   |

## Requisitos

Antes de executar o projeto, certifique-se de possuir:

- [Java JDK 16 ou superior](https://www.oracle.com/java/technologies/downloads/).
- Windows Terminal ou PowerShell.
- Terminal com suporte a UTF-8 e sequências ANSI.
- Janela de terminal com pelo menos **120 colunas por 30 linhas**.

Para verificar a instalação do Java, execute:

```
java -version
javac -version
```

## Como executar

### 1. Clone o repositório

```
git clone https://github.com/cauakssz/rayrtracing-poo.git
```

### 2. Entre na pasta do projeto

```
cd rayrtracing-poo
```

### 3. Compile os arquivos

```
javac -encoding UTF-8 -d . raytrace/main.java raytrace/vec3.java raytrace/ray.java
```

Se nenhum erro for apresentado, a compilação foi concluída com sucesso.

### 4. Configure a codificação do terminal

No PowerShell, execute:

```
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
```

### 5. Execute o programa

```
java -cp . raytrace.Main
```

**Importante:** execute os comandos a partir da pasta raiz do projeto, e não de dentro da pasta `raytrace`.

## Execução simplificada no Windows

Também é possível utilizar o arquivo `make.bat`, caso esteja presente na raiz do projeto.

Basta dar dois cliques no arquivo para iniciar o processo de compilação e execução.

## Conceitos de POO aplicados

### Classes e objetos

Cada elemento da cena é uma classe, e a cena é montada criando objetos dessas classes:

```java
static Camera camera = new Camera(new vec3(-4.0f, 1.5f, 0.0f), new vec3(1.0f, -0.1f, 0.0f).norm());
static Light light   = new Light(new vec3(3.6f, 1.5f, 0.0f), 0.3f);
static Sphere sphere = new Sphere(new vec3(0.0f, 2.0f, 0.0f), vec3.ZERO, 0.9f);
static Plane plane   = new Plane();
```

Cada objeto guarda seus próprios dados (**atributos**) e sabe executar suas próprias ações (**métodos**). A esfera, por exemplo, guarda centro, velocidade e raio, e sabe se atualizar (`update`) e projetar sombra (`shadowOn`).

### Encapsulamento

Os atributos das classes são `private`. Quem está fora só acessa os dados por métodos como `getX()`, `getO()` e `getPosition()`. Assim, cada classe controla como seus dados são usados: a posição da esfera, por exemplo, só muda dentro do `update()`, nunca por código externo.

### Imutabilidade

As classes `vec3` e `ray` têm todos os atributos `private final`: depois de criado, um vetor ou raio nunca muda. Toda operação devolve um **novo** objeto:

```java
vec3 novo = a.add(b);    // a e b continuam iguais
```

Quando a esfera quica, o código não altera o `y` do vetor. Ele cria um novo com `withY()`:

```java
velocity = velocity.withY(velocity.getY() * -1);
```

Isso evita erros por alteração acidental, e por isso as constantes `vec3.UP`, `vec3.DOWN` e `vec3.ZERO` podem ser compartilhadas com segurança.

### Métodos de instância

As operações com vetores pertencem ao próprio vetor. Em vez de `add(a, b)`, escrevemos `a.add(b)`, e é possível encadear operações:

```java
viewRay.getO().add(viewRay.getD().scale(distanceAlongRayToSphere))
```

### Interface e polimorfismo

A interface `Surface` define um contrato: toda superfície precisa saber calcular se um raio a atinge.

```java
interface Surface {
    Optional<ray> intersect(ray viewRay);
}
```

`Sphere` e `Plane` **implementam** essa interface, cada uma com sua própria matemática (equação de segunda ordem para a esfera, altura `y = 0` para o plano). O resultado é o mesmo tipo de resposta: um `Optional<ray>` com o ponto de contato e a normal, ou vazio se o raio não acertou.

### Responsabilidade única

Cada classe cuida de uma coisa só:

- `Light` cuida da luz.
- `Sphere` cuida da esfera e da física dela.
- `Plane` cuida do chão.
- `Camera` cuida do ponto de vista.
- `Main` junta tudo e desenha na tela.

### Composição

O programa final é formado pela **combinação** dos objetos. O `shade` usa `camera`, `light`, `sphere` e `plane` juntos para decidir o brilho de cada ponto da tela, sem que uma classe precise conhecer os detalhes internos da outra.

### Uso de `Optional`

Em vez de devolver `true` ou `false` e preencher um parâmetro, os métodos `intersect` devolvem um `Optional<ray>`. Ele representa claramente "pode haver um resultado ou não", e o código consulta com `isPresent()` e `isEmpty()`.

## Como o código funciona

### Visão geral do ciclo

O `main` repete, por cerca de 30 segundos, três passos:

1. Espera ~33 ms (aproximadamente 30 quadros por segundo).
2. `drawScreen()`: desenha a imagem atual no terminal.
3. `animate()`: atualiza a cena (esfera e câmera) para o próximo quadro.

### Desenhando um quadro (`drawScreen`)

A tela tem 120 colunas por 30 linhas. Para cada posição `(cx, cy)`:

1. `camera.viewRay(cx, cy)` cria um **raio** que sai do olho da câmera e passa por aquele ponto de uma tela virtual colocada à frente dela.
2. `shade(raio)` descobre quão claro é o ponto que o raio enxerga, de 0 a 9.
3. O valor escolhe um caractere da tabela `SHADES`, e todos os caracteres são montados em um `StringBuilder` e impressos de uma só vez. O código ANSI `\033[H` leva o cursor ao topo, então cada quadro é desenhado por cima do anterior.

A altura usa `ROWS*2` no cálculo do raio porque um caractere do terminal é mais ou menos duas vezes mais alto do que largo. Essa correção evita que a esfera apareça achatada.

### A câmera (`Camera`)

A câmera guarda a posição do olho (`eye`) e a direção do olhar (`view`). A partir da direção, ela calcula dois eixos da tela virtual: `screenLeft` (para o lado) e `screenUp` (para cima). O raio de cada pixel é a direção do olhar somada a um deslocamento lateral e vertical, e depois normalizada.

Para girar, `rotateY` rotaciona olho e direção ao redor do eixo vertical e recalcula os eixos da tela.

### Decidindo o brilho de um pixel (`shade`)

O `shade` testa o que o raio atinge, nesta ordem:

1. **A luz.** Se o raio passa perto da fonte de luz (`light.intersects`), o pixel recebe brilho máximo (9). Isso só vale se a esfera não estiver na frente ou se a câmera estiver do lado positivo do eixo X, onde a luz sempre aparece.
2. **A esfera.** O brilho depende do ângulo entre a normal da superfície e a direção da luz, que é o produto escalar `normal · direçãoDaLuz`. Quanto mais a superfície "olha" para a luz, mais clara ela é.
3. **O plano (chão).** O brilho base diminui conforme o ponto se afasta da luz. Depois, `plane.shadowRay` cria um raio de sombra do ponto do chão até a luz, e `sphere.shadowOn` calcula quanto a esfera bloqueia esse raio. O brilho final é o brilho base menos a sombra.
4. **Nada.** O pixel fica escuro (0).

Por fim, se o raio não atingiu a esfera e o brilho está baixo, é somado o efeito de **bloom** (`light.bloom`): um brilho suave ao redor da luz que diminui com a distância ao raio.

### Interseção raio–esfera (`Sphere.intersect`)

Um ponto do raio é `origem + t · direção`. Impondo que ele esteja a uma distância igual ao raio da esfera do centro, obtém-se uma equação de segundo grau em `t`.

- Se o **discriminante** é negativo, o raio não toca a esfera.
- Caso contrário, é escolhida a menor raiz, que é o ponto mais próximo da câmera.
- Se `t` for negativo, a esfera está atrás da câmera e não conta.

A normal no ponto de contato é o vetor do centro até o ponto, normalizado.

### Interseção raio–plano (`Plane.intersect`)

O chão é o plano `y = 0`. A distância até ele é `t = -origem.y / direção.y`. Se o raio é quase paralelo ao chão (`|direção.y|` muito pequeno) ou o chão está atrás da câmera, não há interseção.

### Sombra (`Sphere.shadowOn`)

Para um ponto do chão, o raio de sombra vai em direção à luz. A esfera projeta sombra quando passa perto desse raio: a distância entre o centro da esfera e o raio (calculada pelo produto vetorial) define a intensidade, e quanto mais perto, mais escura a sombra. Se a esfera estiver além da luz, ela não faz sombra.

### Movimento da esfera (`Sphere.update`)

A cada quadro:

1. A posição soma a velocidade e a gravidade.
2. A velocidade aumenta com a gravidade, que aponta para baixo.
3. Se o centro ficar abaixo do raio da esfera (tocou o chão), a velocidade vertical é invertida e a esfera é recolocada logo acima do chão, o que produz o quique.

## Detalhes da implementação

### Renderização

A imagem é construída por meio de uma matriz de caracteres, na qual cada caractere representa um determinado nível de intensidade luminosa.

São utilizados caracteres Unicode de sombreamento:

| Caractere | Intensidade            |
|-----------|------------------------|
| Espaço    | Ausência de iluminação |
| ░         | Baixa                  |
| ▒         | Média                  |
| ▓         | Alta                   |
| █         | Máxima                 |

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

| Problema                            | Possível solução                                                              |
|-------------------------------------|-------------------------------------------------------------------------------|
| `javac is not recognized`           | Instale o JDK e configure a variável PATH.                                    |
| `Could not find or load main class` | Execute o programa na pasta raiz e utilize `raytrace.Main`.                   |
| Caracteres aparecem incorretamente  | Configure a saída do terminal para UTF-8.                                     |
| Imagem aparece desorganizada        | Maximize o terminal e verifique se ele possui pelo menos 120 × 30 caracteres. |
| Animação não é exibida corretamente | Utilize Windows Terminal ou PowerShell com suporte a sequências ANSI.         |

Para interromper a execução antes do término, pressione `Ctrl + C`.

## Objetivo acadêmico

Este projeto tem como objetivo explorar conceitos de computação gráfica e programação orientada a objetos utilizando Java.

Durante sua implementação, são trabalhados conceitos como:

- Classes, objetos, atributos e métodos.
- Encapsulamento e imutabilidade.
- Interfaces e polimorfismo.
- Responsabilidade única e composição.
- Vetores e operações matemáticas tridimensionais.
- Geometria analítica.
- Rastreamento de raios.
- Interseções geométricas.
- Iluminação e sombreamento.
- Simulação física básica.
- Animação e renderização no terminal.
