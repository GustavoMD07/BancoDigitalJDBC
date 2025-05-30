CREATE OR REPLACE FUNCTION public.atualizar_conta_v1(
	p_id bigint,
	p_cliente_id bigint,
	p_tipo_de_conta character varying)
    RETURNS BOOLEAN
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
	UPDATE conta
	SET cliente_id = p_cliente_id,
	tipo_de_conta = p_tipo_de_conta
	WHERE id = p_id;

	IF FOUND THEN
		RETURN true;
	ELSE
		RETURN false;
	END IF;
END;
$BODY$;