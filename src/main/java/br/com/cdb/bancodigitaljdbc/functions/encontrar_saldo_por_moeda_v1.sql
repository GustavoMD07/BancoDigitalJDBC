CREATE OR REPLACE FUNCTION public.encontrar_saldo_por_moeda_v1(p_moeda CHARACTER VARYING(255),p_conta_id BIGINT)
RETURNS TABLE (
	id BIGINT,
	moeda CHARACTER VARYING(255),
	saldo NUMERIC(38,2),
	conta_id BIGINT
)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	RETURN QUERY
	SELECT s.id, s.moeda, s.saldo, s.conta_id
	FROM saldo_moeda s
	WHERE s.conta_id = p_conta_id AND s.moeda = p_moeda;
END;
$BODY$;