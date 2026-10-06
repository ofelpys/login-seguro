package br.com.felipe.login_seguro;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LoginSeguroApplicationTests {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	void deveGravarLerERemoverNoAtlas() {
		MongoCollection<Document> colecao =
				mongoTemplate.getCollection("teste_conexao");

		String id = UUID.randomUUID().toString();
		Document filtro = new Document("_id", id);

		Document registro = new Document("_id", id)
				.append("mensagem", "Conexao funcionando");

		colecao.insertOne(registro);

		try {
			Document encontrado = colecao.find(filtro).first();

			assertNotNull(encontrado);
			assertEquals(
					"Conexao funcionando",
					encontrado.getString("mensagem")
			);
		} finally {
			long removidos = colecao.deleteOne(filtro).getDeletedCount();
			assertEquals(1L, removidos);
		}
	}
}