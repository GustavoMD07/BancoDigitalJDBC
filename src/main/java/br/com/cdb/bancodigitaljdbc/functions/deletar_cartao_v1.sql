CREATE OR REPLACE FUNCTION public.deletar_cartao_v1(
	p_id bigint)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
	DELETE FROM cartao WHERE id = p_id;
	
	IF FOUND THEN
		RETURN true;
	ELSE
		RETURN false;
	END IF;
END;
$BODY$;