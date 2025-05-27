CREATE OR REPLACE FUNCTION public.encontrar_todos_conta_v1()
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
	SELECT id, cliente_id, tipo_de_conta, taxa_rendimento, taxa_manutencao
	FROM conta ;
END;
$BODY$;