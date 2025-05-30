CREATE OR REPLACE FUNCTION public.atualizar_saldo_v1(
	p_saldo numeric,
	p_id bigint)
    RETURNS BOOLEAN
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	UPDATE saldo_moeda
	SET saldo = p_saldo
	WHERE id = p_id;

	IF FOUND THEN
		RETURN TRUE;
	ELSE
		RETURN FALSE;
	END IF;
END;
$BODY$;