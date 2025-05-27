CREATE OR REPLACE FUNCTION public.atualizar_cartao_v1(
	p_id BIGINT,
	p_senha character varying,
	p_status boolean,
	p_num_cartao character varying,
	p_tipo_de_cartao character varying,
	p_conta_id bigint,
	p_fatura numeric,
	p_limite_credito numeric,
	p_limite_diario numeric
)
RETURNS void
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	UPDATE cartao
	SET senha = p_senha,
	status = p_status,
	num_cartao = p_num_cartao, 
	tipo_de_cartao = p_tipo_de_cartao,
	conta_id = p_conta_id,
	fatura = p_fatura,
	limite_credito = p_limite_credito,
	limite_diario = p_limite_diario
	WHERE id = p_id;
END;
$BODY$;