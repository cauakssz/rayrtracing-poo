# ascii-raytrace (versão em Java)

Ray tracing no terminal, em ASCII: uma bola quicando ao lado de uma luz, com a câmera girando em volta.

## Arquivos

```
raytrace/
├── main.java   (programa principal)
├── vec3.java   (vetores 3D)
└── ray.java    (raio)
```

## O que você precisa

- JDK 16 ou mais novo instalado (teste com `javac -version`).
- Windows Terminal ou PowerShell.

## Como rodar (PowerShell)

Copie e cole os comandos abaixo, um de cada vez.

**1. Entre na pasta do projeto** (a pasta que contém a pasta `raytrace`). Troque o caminho pelo seu:

```
cd C:\Users\seunome\Documents\rayrtracing-poo-main
```

**2. Compile:**

```
javac -encoding UTF-8 raytrace\main.java raytrace\vec3.java raytrace\ray.java
```

Se não aparecer nada, compilou com sucesso.

**3. Deixe o terminal em UTF-8** (para os blocos aparecerem direito):

```
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
```

**4. Maximize a janela do terminal.** O programa precisa de pelo menos 120 colunas por 30 linhas.

**5. Rode:**

```
java raytrace.main
```

A animação dura cerca de 30 segundos e termina sozinha. Para parar antes, aperte `Ctrl+C`.

## Problemas comuns

- **`'javac' is not recognized`**: o JDK não está instalado ou não está no PATH.
- **`Could not find or load main class raytrace.main`**: você está dentro da pasta `raytrace`. Volte um nível com `cd ..` e rode de novo. Se a pasta estiver certa, veja o nome do `.class` gerado com `dir raytrace`: se for `Main.class`, rode `java raytrace.Main`.
- **Imagem quebrada ou bagunçada**: a janela está menor que 120x30. Maximize e rode de novo.
- **Símbolos estranhos no lugar dos blocos**: faltou o passo 3.
