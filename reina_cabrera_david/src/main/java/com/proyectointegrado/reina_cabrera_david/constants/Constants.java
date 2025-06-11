package com.proyectointegrado.reina_cabrera_david.constants;

/**
 * The Class Constants
 */
public class Constants {

	/** The Constant RESERVATION_SAVED_HTML_MAIL_TEMPLATE */
	public static final String RESERVATION_SAVED_HTML_MAIL_TEMPLATE = """
			    <html>
			    <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
			        <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px #cccccc;">
			            <h2 style="color: #333;">📅 Reserva de clase confirmada</h2>
			            <p>Hola %s,</p>
			            <p>Tu reserva para la clase de <strong>%s</strong> el día <strong>%s</strong> 
			            a las <strong>%s</strong> horas, ha sido <strong>agendada correctamente</strong>.</p>
			            <p>Te esperamos con entusiasmo para tu próxima clase.</p>
			            <hr style="margin: 20px 0;">
			            <p style="font-size: 14px; color: #555;">
			                Si tienes alguna pregunta, no dudes en contactarnos.<br>
			                <strong>StepFlow Organization</strong><br>
			                <a href="mailto:stepfloworganization@gmail.com">stepfloworganization@gmail.com</a>
			            </p>
			        </div>
			    </body>
			    </html>
			""";

	/** The Constant RESERVATION_CANCELLED_HTML_MAIL_TEMPLATE */
	public static final String RESERVATION_CANCELLED_HTML_MAIL_TEMPLATE = """
		    <html>
		    <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
		        <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px #cccccc;">
		            <h2 style="color: #d9534f;">❌ Reserva de clase cancelada</h2>
		            <p>Hola %s,</p>
		            <p>Tu reserva para la clase de <strong>%s</strong> el día <strong>%s</strong> 
		            a las <strong>%s</strong> horas, ha sido <strong>cancelada exitosamente</strong>.</p>
		            <p>Si esta cancelación fue un error o deseas reprogramar tu clase, no dudes en contactarnos.</p>
		            <hr style="margin: 20px 0;">
		            <p style="font-size: 14px; color: #555;">
		                Gracias por confiar en nosotros.<br>
		                <strong>StepFlow Organization</strong><br>
		                <a href="mailto:stepfloworganization@gmail.com">stepfloworganization@gmail.com</a>
		            </p>
		        </div>
		    </body>
		    </html>
		""";
	
	/** The Constant CLASS_CANCELLED_HTML_MAIL_TEMPLATE */
	public static final String CLASS_CANCELLED_HTML_MAIL_TEMPLATE = """
		    <html>
		    <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
		        <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px #cccccc;">
		            <h2 style="color: #d9534f;">❌ Clase cancelada</h2>
		            <p>Hola %s,</p>
		            <p>Te informamos que la clase de <strong>%s</strong> programada para el día <strong>%s</strong> 
		            a las <strong>%s</strong> horas ha sido <strong>cancelada</strong>.</p>
		            <p>Lamentamos los inconvenientes que esto pueda ocasionar y te invitamos a ponerte en contacto con nosotros para cualquier duda o para reprogramar.</p>
		            <hr style="margin: 20px 0;">
		            <p style="font-size: 14px; color: #555;">
		                Gracias por confiar en nosotros.<br>
		                <strong>StepFlow Organization</strong><br>
		                <a href="mailto:stepfloworganization@gmail.com">stepfloworganization@gmail.com</a>
		            </p>
		        </div>
		    </body>
		    </html>
		""";
}
