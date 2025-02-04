# SocialMedia: Guerreiros da Orientação a Objetos

## 💡 **Equipe de Desenvolvedores:**
- **Thiago Ferreira dos Santos**
- **Felipe Meneguzzi**
- **João Pedro Murari**
- **Caio César Sifuentes Barcelos**

# 🛠️ Configuração do Banco de Dados MongoDB através do MongoDB Compass

---

## 🎯 **Pré-requisitos**
Certifique-se de que o MongoDB está instalado e rodando em `localhost:27017`.

### Instalar o MongoDB
- [Download do MongoDB](https://www.mongodb.com/try/download/community)
- [Download do MongoDB Compass](https://www.mongodb.com/products/compass)

---

## ⚙️ **Passo a Passo**

1. **Abrir o MongoDB Compass**  
   Inicie o MongoDB Compass e conecte-se usando a seguinte URI:
   ```
   mongodb://localhost:27017
   ```

2. **Criar o Banco de Dados**
    - Clique em **Create Database**.
    - Insira o nome do banco como `socialmedia_mongodb`.
    - Informe qualquer nome para a coleção inicial, por exemplo, `posts`.
    - Clique em **Create Database**.

3. **Verificar o Banco**  
   Certifique-se de que o banco foi criado e está visível na lista de databases.

---

## 🧪 **Testar a Conexão com a Aplicação Spring Boot**
Verifique se o arquivo `application.properties` da aplicação Spring Boot esteja configurado configure:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/socialmedia_mongodb
```
