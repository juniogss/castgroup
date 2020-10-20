# Cast group

O app possui as funcionalidades de inserir, listar, remover e editar categorias e inserir, listar, remover e editar cursos.

<img src="https://user-images.githubusercontent.com/56129260/96529998-dd0f7100-125c-11eb-9087-d54138a33c32.png" width="200"> <img src="https://user-images.githubusercontent.com/56129260/96530420-c6b5e500-125d-11eb-9afd-43d643c8d8c1.png" width="200">
<img src="https://user-images.githubusercontent.com/56129260/96530478-ecdb8500-125d-11eb-9ec6-b05522901478.png" width="200"> <img src="https://user-images.githubusercontent.com/56129260/96530546-0bda1700-125e-11eb-862f-bddbdd1498f8.png" width="200">

Também possui a opção filtrar, onde é possível buscar cursos pela descrição ou data de início, e ordenar alfabetica ou cronologicamente.

<img src="https://user-images.githubusercontent.com/56129260/96530771-81de7e00-125e-11eb-9e6d-61e796c8ce11.png" width="200">

# Especificações técnicas
- Retrofit pras requisições HTTP
- SQLite para armazenamento local no app e MongoDB para armazenamento no servidor.
- Arquitetura MVP.
- Componentes UI com Material Design e construção de componentes estilizados.
- Stetho para ponte de depuração

## Uso

- instalar o nodejs (https://nodejs.org/en/)
- instalar o mongoDB (https://www.mongodb.com/)
- acessar a pasta backend e executar o comando npm install no terminal
- renomear o arquivo .env.example para .env e colocar os dados de conexão ao banco de dados do mongoDB
- executar o comando npm start no terminal

Substituir o 'localhost' pelo IP local da máquina, no arquivo /common/datasource/remote/LocalAPI 

```java

    String BASE_URL = "http://localhost:3333/";
```
