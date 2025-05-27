CREATE OR REPLACE FUNCTION public.encontrar_conta_v1(p_id bigint)
    RETURNS TABLE(
	id bigint, 
	cliente_id bigint, 
	tipo_de_conta character varying, 
	taxa_rendimento numeric, 
	taxa_manutencao numeric
	) 
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	RETURN QUERY
	SELECT s.id, s.cliente_id, s.tipo_de_conta, s.taxa_rendimento, s.taxa_manutencao
	FROM conta s
	WHERE s.id = p_id;
END;
$BODY$;
