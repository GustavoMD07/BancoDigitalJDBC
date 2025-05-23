CREATE OR REPLACE PROCEDURE public.inserir_cartao_v1(
	p_senha CHARACTER VARYING(255),
	p_status BOOLEAN,
	p_num_cartao CHARACTER VARYING(255),
	p_tipo_de_cartao CHARACTER VARYING(31),
	p_conta_id BIGINT,
	p_fatura NUMERIC (38,2),
	p_limite_credito NUMERIC (38,2),
	p_limite_diario NUMERIC (38,2)
)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
		INSERT INTO cartao(
		senha,
		status,
		num_cartao,
		tipo_de_cartao,
		conta_id,
		fatura,
		limite_credito,
		limite_diario
		)
		VALUES (
		p_senha,
		p_status,
		p_num_cartao,
		p_tipo_de_cartao,
		p_conta_id,
		p_fatura,
		p_limite_credito,
		p_limite_diario
		);
END
$BODY$