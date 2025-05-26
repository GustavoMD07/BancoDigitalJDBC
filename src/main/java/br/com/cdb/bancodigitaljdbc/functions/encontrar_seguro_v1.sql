CREATE OR REPLACE FUNCTION public.encontrar_seguro_v1(p_id BIGINT)
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
	SELECT s.id, s.ativo, s.data_contratacao, s.descricao, s.numero_apolice, s.tipo_de_seguro, s.valor_apolice, s.cartao_id
	FROM seguro s
	WHERE s.id = p_id;
END;
$BODY$;
