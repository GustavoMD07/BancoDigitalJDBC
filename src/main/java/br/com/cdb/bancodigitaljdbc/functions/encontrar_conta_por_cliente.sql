CREATE OR REPLACE FUNCTION public.encontrar_conta_por_cliente(p_cliente_id BIGINT)
RETURNS TABLE(
	cliente_id bigint,
	tipo_de_conta character varying,
	taxa_rendimento numeric,
	taxa_manutencao numeric
)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	RETURN QUERY
	SELECT s.cliente_id, s.tipo_de_conta, s.taxa_rendimento, s.taxa_manutencao
	FROM conta s
	WHERE s.cliente_id = p_cliente_id;
END;
$BODY$;