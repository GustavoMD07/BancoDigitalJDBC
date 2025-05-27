CREATE OR REPLACE FUNCTION public.encontrar_todos_seguros_v1(
	)
    RETURNS TABLE(id bigint, ativo boolean, data_contratacao date, descricao character varying, numero_apolice character varying, tipo_de_seguro character varying, valor_apolice numeric, cartao_id bigint) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
	RETURN QUERY
	SELECT s.id, s.ativo, s.data_contratacao, s.descricao, s.numero_apolice, s.tipo_de_seguro, s.valor_apolice, s.cartao_id
	FROM seguro s;
END;
$BODY$;