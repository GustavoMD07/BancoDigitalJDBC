CREATE OR REPLACE FUNCTION public.deletar_saldo_por_conta_v1(p_conta_id bigint)
RETURNS VOID
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	DELETE FROM saldo_moeda WHERE conta_id = p_conta_id;
END;
$BODY$;