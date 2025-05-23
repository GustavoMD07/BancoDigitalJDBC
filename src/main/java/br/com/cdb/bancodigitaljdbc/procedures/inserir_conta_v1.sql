CREATE OR REPLACE PROCEDURE public.inserir_conta_v1(
	IN p_cliente_id BIGINT,
	IN p_tipo_de_conta CHARACTER VARYING(31),
	IN taxa_rendimento NUMERIC(6,3),
	IN taxa_manutencao NUMERIC(6,3)
)
LANGUAGE 'plpgsql'
AS $BODY$
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
		);
END
$BODY$