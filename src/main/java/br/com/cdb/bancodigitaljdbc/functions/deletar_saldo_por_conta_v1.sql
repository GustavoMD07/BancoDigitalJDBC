CREATE OR REPLACE FUNCTION public.deletar_saldo_por_conta_v1(
	p_conta_id bigint)
    RETURNS boolean
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	DELETE FROM saldo_moeda WHERE conta_id = p_conta_id;
	IF FOUND THEN
		RETURN TRUE;
	ELSE
		RETURN FALSE;
	END IF;
END;
$BODY$;