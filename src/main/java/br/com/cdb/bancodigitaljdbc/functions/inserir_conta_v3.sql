CREATE OR REPLACE FUNCTION public.inserir_conta_v3(
	 p_cliente_id bigint,
	 p_tipo_de_conta character varying,
	 p_taxa_rendimento numeric,
	 p_taxa_manutencao numeric
	 )
	RETURNS BIGINT
	
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE 
	conta_id BIGINT;
BEGIN
		INSERT INTO conta(
		cliente_id,
		tipo_de_conta,
		taxa_rendimento,
		taxa_manutencao
		)
		VALUES(
		p_cliente_id,
		p_tipo_de_conta,
		p_taxa_rendimento,
		p_taxa_manutencao
		) RETURNING id into conta_id;
		RETURN conta_id;
END
$BODY$;