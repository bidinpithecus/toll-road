# README

## Descrição

Para compilar e executar basta rodar o script **run**. Este script é utilizado para iniciar um servidor Java que simula o processamento de um número específico de carros ao longo de intervalos de tempo determinados. Ele requer três parâmetros de entrada:

1. **totalNumOfCars**: O número total de carros a serem processados.
2. **numOfCarsPerInterval**: O número de carros processados em cada intervalo.
3. **interval**: O tempo (em milissegundos) entre cada intervalo de processamento.

O script também cria pastas e arquivos de banco de dados e logs necessários para o funcionamento do servidor, e utiliza bibliotecas RabbitMQ para a comunicação.

## Pré-requisitos

Antes de rodar o script, certifique-se de ter:

- **Bibliotecas RabbitMQ**: 
  - `amqp-client-4.0.2.jar`
  - `slf4j-api-1.7.21.jar`
  - `slf4j-simple-1.7.22.jar`
  
  Essas bibliotecas devem estar localizadas no diretório `/usr/local/rabbitmq-jar/`. Se não estiverem, modifique o caminho no script para apontar para o diretório correto onde elas estão instaladas.
## Logs e Banco de Dados
**Logs**: Os arquivos de logs são armazenados no diretório logs/. Eles armazenam informações sobre o processamento do servidor e os eventos durante a simulação:


**Banco de Dados**: O diretório database/ contém os arquivos de banco de dados, que registram as informações relacionadas aos carros processados durante a execução:
