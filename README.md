# Microsserviços — currency-report e currency-history

Este projeto implementa dois microsserviços independentes em **Spring Boot**, cada um com seu próprio Dockerfile e seu próprio `docker-compose.yml`, porém compartilhando uma mesma rede Docker externa (`currency-net`).  
Essa rede permite que os dois microsserviços se comuniquem entre si usando HTTP.

Os serviços fornecem dados mockados e incluem endpoints adicionais que demonstram **integração real entre microsserviços**, conforme boas práticas da arquitetura.

---

# 📌 Microsserviço A — currency-report
Função: fornecer informações de cotação atual.

### Endpoints
- `GET /health`
- `GET /quote?from=USD&to=BRL`
- `GET /history-from-report` *(integração: chama o history)*

---

# 📌 Microsserviço B — currency-history
Função: fornecer histórico recente de valores.

### Endpoints
- `GET /health`
- `GET /history?from=USD&to=BRL`
- `GET /quote-from-history` *(integração: chama o report)*

---

# 🚀 Como subir o ambiente

Cada microsserviço possui **seu próprio docker-compose**, e ambos compartilham a rede externa `currency-net`.

### 1️⃣ Criar a rede externa (apenas uma vez)

```bash
docker network create currency-net
