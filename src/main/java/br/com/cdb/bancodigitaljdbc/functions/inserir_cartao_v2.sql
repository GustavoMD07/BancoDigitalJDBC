CREATE OR REPLACE FUNCTION public.inserir_cartao_v2(
	p_senha character varying,
	p_status boolean,
	p_num_cartao character varying,
	p_tipo_de_cartao character varying,
	p_conta_id bigint,
	p_fatura numeric,
	p_limite_credito numeric,
	p_limite_diario numeric)
	RETURNS BIGINT
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
	cartao_id BIGINT;
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
		) RETURNING ID INTO cartao_id;
		RETURN cartao_id;
END
$BODY$;