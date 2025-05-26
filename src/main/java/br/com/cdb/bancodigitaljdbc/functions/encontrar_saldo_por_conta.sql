CREATE OR REPLACE FUNCTION public.encontrar_saldo_por_conta(
	p_conta_id bigint)
    RETURNS TABLE(id bigint, moeda character varying, saldo numeric, conta_id bigint) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
	RETURN QUERY
	SELECT s.id, s.moeda, s.saldo, s.conta_id
	FROM saldo_moeda s
	WHERE s.conta_id = p_conta_id;
END;
$BODY$;
