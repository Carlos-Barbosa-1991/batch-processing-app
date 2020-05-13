# Processamento de Arquivos em Lote

## Requisitos para executar o programa
1. Ter o java 8 instalado e configurado na máquina;
2. Ter o Maven 3 instalado;
3. Inserir arquivos ".dat" no caminho %HOMEPATH%\data\in\ com o layout abaixo.

**Obs.: Caso o caminho não exista, deve ser criado.**

**Layout esperado:**

001ç1234567891234çPedroç50000  
001ç3245678865434çPauloç40000.99  
002ç2345675434544345çJose da SilvaçRural  
002ç2345675433444345çEduardo PereiraçRural  
003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro  
003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo  

## Instruções de uso:
1. Baixe o arquivo zip e extraia na sua máquina;
2. Abra seu terminal preferido. (Terminal, Prompt, PowerShell...);
3. Navegue com seu terminal até a pasta do projeto, na raiz se encontrará o arquivo pom.xml;
4. Execute o comando "mvn clean install" para compilar o projeto;
5. Navegue com seu terminal até a pasta "target" do projeto, lá se encontrará o arquivo .jar;
6. escreva no terminal: java -jar batch-processing-app-3.3.3-SNAPSHOT.jar e não feche o terminal aberto;

Qualquer dúvida ou problema, entrar em contato com carlosbarbosasama@gmail.com.

## Instruções para visualização de código:
O projeto pode ser aberto em qualquer IDE que rode java, sendo Visual Studio Code, Eclipse, NetBeans. 
O programa foi desenvolvido no IntelliJ, usando Linux Mint XFCE.