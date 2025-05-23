CREATE OR REPLACE PROCEDURE public.inserir_cliente_v1(
IN p_nome CHARACTER VARYING(255),
IN p_cpf CHARACTER VARYING(255),
IN p_data_nascimento DATE,
IN p_cep CHARACTER VARYING(255),
IN p_rua CHARACTER VARYING(255),
IN p_bairro CHARACTER VARYING(255),
IN p_cidade CHARACTER VARYING(255),
IN p_estado CHARACTER VARYING(255),
IN p_tipo_de_cliente CHARACTER VARYING(31)
)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
		INSERT INTO cliente(
			nome,
			cpf,
			data_nascimento,
			cep,
			rua,
			bairro,
			cidade,
			estado,
			tipo_de_cliente
		)
		VALUES (
			p_nome,
			p_cpf,
			p_data_nascimento,
			p_cep,
			p_rua,
			p_bairro,
			p_cidade,
			p_estado,
			p_tipo_de_cliente
		);
END
$BODY$