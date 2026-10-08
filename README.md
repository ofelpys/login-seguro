# Login Seguro

Projeto desenvolvido para a atividade de Aplicativos Web, usando Java, Spring Boot, Spring Security, Thymeleaf e MongoDB Atlas. O sistema permite cadastrar usuários, fazer login e logout e controlar o acesso às páginas por perfil. A estrutura separa a interface das regras de negócio para facilitar futuras adaptações ao PFC.

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring Security
- Spring Data MongoDB
- Thymeleaf
- Bean Validation
- MongoDB Atlas
- Maven Wrapper

## Funcionalidades

- Cadastro com validação dos campos e bloqueio de e-mails duplicados.
- Login com e-mail e senha.
- Senhas armazenadas como hash BCrypt.
- Logout com encerramento da sessão.
- Controle de acesso com os perfis USER, MANAGER e ADMIN.
- Listagem, edição e exclusão de usuários pelo administrador.
- Armazenamento de usuários e sessões no MongoDB Atlas.
- Cabeçalho e rodapé compartilhados entre as páginas.
- Personalização do nome do sistema e das cores por configuração.

## Preparação do ambiente

É necessário ter o JDK 21 instalado, acesso à internet e um cluster no MongoDB Atlas. O projeto inclui o Maven Wrapper, então não é necessário instalar o Maven separadamente.

Para baixar a versão de desenvolvimento:

```bash
git clone --branch develop https://github.com/ofelpys/login-seguro.git
cd login-seguro
```

Abra a pasta que contém o arquivo `pom.xml` na IDE e aguarde o carregamento das dependências.

## Configuração do MongoDB Atlas

1. Crie um cluster no Atlas.
2. Crie um usuário de banco com permissão de leitura e escrita no banco `login_seguro`.
3. Adicione o IP da máquina que executará a aplicação à lista de acesso de rede.
4. Na opção de conexão para aplicações, copie a URI fornecida pelo Atlas.
5. Preencha o usuário e a senha do banco na URI e configure a variável de ambiente `MONGODB_URI`.

Exemplo de formato, com valores fictícios:

```text
MONGODB_URI=mongodb+srv://USUARIO:SENHA@SEU_CLUSTER.mongodb.net/?retryWrites=true&w=majority
```

Caracteres especiais no usuário ou na senha precisam ser codificados para uso na URI. Utilize a conexão segura fornecida pelo Atlas e mantenha a validação TLS habilitada.

As credenciais devem ficar nas variáveis de ambiente da máquina ou da IDE. Não devem ser colocadas no código nem enviadas ao GitHub.

O arquivo `src/main/resources/application.properties` contém:

```properties
spring.application.name=login-seguro
spring.mongodb.uri=${MONGODB_URI}
spring.mongodb.database=login_seguro
spring.data.mongodb.auto-index-creation=true

app.visual.nome=Login Seguro
app.visual.tema=padrao
```

O banco utilizado é `login_seguro`, com as coleções:

- `usuarios`: dados cadastrais, perfil e hash da senha.
- `sessoes`: dados necessários para manter as sessões dos usuários.

O e-mail possui um índice único no banco para impedir cadastros duplicados.

## Execução pela IDE

No IntelliJ, abra as configurações de execução da classe `LoginSeguroApplication`. No campo de variáveis de ambiente, adicione `MONGODB_URI` com a URI completa do seu Atlas.

Confira se o projeto está utilizando o JDK 21 e execute a classe `LoginSeguroApplication`.

Depois da inicialização, acesse:

http://localhost:8080/login

## Execução pelo terminal

No PowerShell, dentro da pasta que contém o `pom.xml`, configure a variável para aquela sessão do terminal e execute:

```powershell
$env:MONGODB_URI='mongodb+srv://USUARIO:SENHA@SEU_CLUSTER.mongodb.net/?retryWrites=true&w=majority'
.\mvnw.cmd spring-boot:run
```

Substitua os valores de exemplo pelos dados do seu cluster.

A variável configurada na execução da IDE não é automaticamente aplicada ao terminal. Para encerrar a aplicação iniciada pelo terminal, pressione `Ctrl + C`.

## Perfis de acesso

Todo usuário criado pela página de cadastro recebe o perfil `USER`.

| Perfil | Acesso |
| --- | --- |
| USER | Página inicial após o login |
| MANAGER | Página inicial e área de gerenciamento |
| ADMIN | Página inicial, gerenciamento e administração de usuários |

As principais rotas são:

- `/cadastro`: cadastro público.
- `/login`: autenticação.
- `/inicio`: página para usuários autenticados.
- `/gerenciamento`: acesso de MANAGER e ADMIN.
- `/admin`: acesso de ADMIN.
- `/admin/usuarios`: administração dos usuários.

O logout é feito pelo botão “Sair”, por uma requisição POST.

## Primeiro administrador

Para preparar o primeiro acesso administrativo:

1. Cadastre sua conta pela aplicação.
2. Saia da conta.
3. No Atlas, abra o banco `login_seguro` e a coleção `usuarios`.
4. Localize o documento pelo e-mail cadastrado.
5. Altere o campo `perfil` de `USER` para `ADMIN`.
6. Faça login novamente.

Depois disso, os perfis dos demais usuários podem ser alterados pela área administrativa do sistema.

O administrador não pode excluir a própria conta nem alterar o próprio e-mail ou perfil pela aplicação. Essas alterações exigem outra conta administrativa.

## Senhas e sessões

As senhas são armazenadas com BCrypt, com fator de custo 12. O cadastro exige pelo menos 8 caracteres e respeita o limite de 72 bytes em UTF-8 usado pelo BCrypt.

As sessões são persistidas no Atlas e expiram após 30 minutos de inatividade. Uma sessão válida pode continuar funcionando após a reinicialização da aplicação, desde que o navegador mantenha o cookie correspondente.

O cookie de sessão utiliza HttpOnly e SameSite=Lax. A proteção CSRF permanece habilitada nos formulários.

A exclusão de uma conta ou a alteração de seu e-mail ou perfil pela aplicação marca suas sessões como expiradas. O encerramento é aplicado na próxima requisição do usuário.

## Organização do projeto

O código Java está no pacote `br.com.felipe.login_seguro`.

| Pasta | Responsabilidade |
| --- | --- |
| `config` | Configurações de segurança, senhas, sessões e tema |
| `controller` | Rotas, formulários e dados enviados às páginas |
| `dto` | Dados de entrada e saída e validação dos campos |
| `entity` | Representação dos usuários e dos perfis |
| `mapper` | Conversão entre entidades e DTOs |
| `repository` | Acesso aos dados no MongoDB |
| `service` | Interfaces dos serviços |
| `service/impl` | Implementação das regras de negócio |

Os arquivos da interface ficam em:

- `src/main/resources/templates`: páginas Thymeleaf.
- `src/main/resources/templates/fragmentos`: partes compartilhadas das páginas.
- `src/main/resources/static/css`: estilos.
- `src/main/resources/static/css/temas`: cores dos temas.
- `src/main/resources/static/imagens`: imagens utilizadas no site.

## Personalização

O nome e o tema são definidos no `application.properties`:

```properties
app.visual.nome=Login Seguro
app.visual.tema=verde
```

Os temas disponíveis são `padrao` e `verde`.

Para criar outro tema, adicione um arquivo CSS na pasta de temas e configure seu nome sem a extensão `.css`. O nome deve conter apenas letras minúsculas, números ou hífen.

Os temas utilizam as variáveis:

```css
:root {
    --cor-primaria: #17613a;
    --cor-fundo: #f1f7f2;
    --cor-texto: #203527;
}
```

O cabeçalho, o rodapé e a referência à imagem estão em `templates/fragmentos/layout.html`. Alterações nesse arquivo são aplicadas às páginas que utilizam esses fragmentos.

Essa organização permite adaptar a aparência e o conteúdo ao PFC mantendo os serviços de autenticação, usuários e sessões.

## Verificação manual

Para conferir o funcionamento:

1. Cadastre um usuário e tente repetir o mesmo e-mail.
2. Teste o login com credenciais válidas e inválidas.
3. Confira as permissões dos três perfis, inclusive acessando as URLs diretamente.
4. Teste a edição e a exclusão de usuários pela administração.
5. Confira a criação das sessões no Atlas e o encerramento pelo logout.
6. Reinicie a aplicação com uma sessão válida e confira se o acesso continua funcionando.
7. Altere o tema e confira a apresentação das páginas.

## Organização das branches

O projeto segue a organização do Gitflow:

- `main`: versões finalizadas.
- `develop`: integração das funcionalidades.
- `feature/*`: desenvolvimento de cada funcionalidade.
- `release/*`: preparação de uma entrega.
- `hotfix/*`: correções urgentes de uma versão publicada.

## Documentação acadêmica

O PDF da documentação e seu arquivo editável ficam na pasta `docs`. A documentação apresenta a estrutura do sistema, a integração com o Atlas e as decisões tomadas durante o desenvolvimento.