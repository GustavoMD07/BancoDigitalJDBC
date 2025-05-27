CREATE OR REPLACE FUNCTION public.encontrar_todos_cartao_v1(
	)
    RETURNS TABLE(id bigint, senha character varying, status boolean, num_cartao character varying, tipo_de_cartao character varying, conta_id bigint, fatura numeric, limite_credito numeric, limite_diario numeric) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
	RETURN QUERY
	SELECT s.id, s.senha, s.status, s.num_cartao, s.tipo_de_cartao, s.conta_id, s.fatura, s.limite_credito, s.limite_diario
	FROM cartao s;
END;
$BODY$;