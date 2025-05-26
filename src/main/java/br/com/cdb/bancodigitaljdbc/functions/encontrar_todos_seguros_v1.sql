CREATE OR REPLACE FUNCTION public.encontrar_todos_seguros_v1()
RETURNS TABLE (
	id BIGINT,
	ativo BOOLEAN,
	data_contratacao DATE,
	descricao CHARACTER VARYING(255),
	numero_apolice CHARACTER VARYING(255),
	tipo_de_seguro CHARACTER VARYING(255),
	valor_apolice NUMERIC(38,2),
	cartao_id BIGINT
)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	RETURN QUERY
	SELECT id, ativo, data_contratacao, descricao, numero_apolice, tipo_de_seguro, valor_apolice, cartao_id
	FROM seguro;
END;
$BODY$;