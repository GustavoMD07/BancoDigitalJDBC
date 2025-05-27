CREATE OR REPLACE FUNCTION public.atualizar_conta_v2(
	p_id bigint,
	p_cliente_id bigint,
	p_tipo_de_conta character varying
	)
RETURNS void
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	UPDATE conta
	SET cliente_id = p_cliente_id,
	tipo_de_conta = p_tipo_de_conta
	WHERE id = p_id;
END;
$BODY$;
