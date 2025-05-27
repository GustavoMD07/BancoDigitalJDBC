CREATE OR REPLACE FUNCTION public.encontrar_conta_por_cliente_v1(p_cliente_id bigint)
    RETURNS TABLE(id bigint, cliente_id bigint, tipo_de_conta character varying, taxa_rendimento numeric, taxa_manutencao numeric) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
	RETURN QUERY
	SELECT s.id, s.cliente_id, s.tipo_de_conta, s.taxa_rendimento, s.taxa_manutencao
	FROM conta s
	WHERE s.cliente_id = p_cliente_id;
END;
$BODY$;