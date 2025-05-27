CREATE OR REPLACE FUNCTION public.encontrar_usuario_por_email_v1(p_email CHARACTER VARYING(255))
RETURNS TABLE(
 id BIGINT,
 email CHARACTER VARYING,
 nome CHARACTER VARYING,
 role CHARACTER VARYING,
 senha CHARACTER VARYING
)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	RETURN QUERY
	SELECT s.id, s.email, s.nome, s.role, s.senha 
	FROM usuario s
	WHERE s.email = p_email;
END;
$BODY$;