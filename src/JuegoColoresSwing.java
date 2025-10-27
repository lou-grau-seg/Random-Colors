import java.awt.*; //Color - FlowLayout - Font - Toolkit
import java.awt.event.*; // Action Event - Action Listener - Key Adapter - Key Event
import java.util.Random; //Random
import javax.swing.*; // JButton - JFrame - JLabel - JOPtionPane - JTextField - Timer - BoxLayout


public class JuegoColoresSwing {
    private JFrame frame = new JFrame("RANDOM COLORS"); //este es el frame que sostiene todo

    private JPanel panelBase=new JPanel();
    private JPanel panelColores=new JPanel();
    private JPanel panelRespuestas=new JPanel();
    private JPanel panelStats =new JPanel();
    private JPanel panelTiempo =new JPanel();

    private JTextField respuestaField; //donde ponemos la respuesta
    private JLabel colorLabel; //display del color random
    private JLabel vidasLabel; //display de las vidas disponibles
    private JLabel tiempoLabel; //display del tiempo disponible
    private JLabel puntosLabel; //display de los puntos que conseguimos
    private Colores colores = new Colores(); //objeto colores

    private String usuario;
    private String colorActual;
    private int tiempoLimite = 10; //tiempo que tenemos disponible
    private int vidas = 2; //cantidad de vidas disponibles
    private int puntos; //Los puntos que conseguimos

    private Timer timer;
    private Archivo buscador=new Archivo();


    //constructor de la clase
    public JuegoColoresSwing() {
        //preguntar por usuario
        this.usuario= JOptionPane.showInputDialog("Ingrese su nombre de usuario");
        this.puntos=this.buscador.buscarPuntosUsuario(this.usuario); //busca la puntuacion anterior del usuario
        if(!this.buscador.isPertenece()){ //si el jugador es nuevo
            this.buscador.escribirNuevoContenido(this.usuario);
            JOptionPane.showMessageDialog(null,"Usted no ha jugado con anterioridad","RANDOM COLORS",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        else{ //si el jugador ya habia jugado antes
            JOptionPane.showMessageDialog(null,"Usted ha jugado con anterioridad y posee " + this.puntos +" puntos!",
                    "RANDOM COLORS", JOptionPane.INFORMATION_MESSAGE);
        }
        //establecemos el frame/marco
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //lo que va a pasar cuando se cierre la ventana
        Toolkit miPantalla=Toolkit.getDefaultToolkit();
        int altura=miPantalla.getScreenSize().height;
        int largo=miPantalla.getScreenSize().width;
        this.frame.setSize(largo/2, altura/2); //medidas del frame
        this.frame.setLocation(largo/4, altura/4); //posicionamiento del frame

        //establecemos los Layout
        this.panelBase.setLayout(new BoxLayout(panelBase,BoxLayout.Y_AXIS));
        this.panelStats.setLayout(new FlowLayout());
        this.panelRespuestas.setLayout(new FlowLayout());

        //establecemos los Label
        this.respuestaField = new JTextField(10);
        this.colorLabel = new JLabel("COLOR");
        this.colorLabel.setFont(new Font("Arial", Font.BOLD, 100));
        this.vidasLabel = new JLabel("Vidas: " + this.vidas);
        this.vidasLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        this.puntosLabel = new JLabel("       Puntos: " + this.puntos);
        this.puntosLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        this.tiempoLabel = new JLabel("Tiempo: " + this.tiempoLimite + "s");
        this.tiempoLabel.setFont(new Font("Arial", Font.BOLD, 90));

        JButton enviarButton = new JButton("Enviar");
        /*si se aprieta el boton de enviar respuesta
        esta debe ser verificada. En caso de ser incorrecta esta lanzara una excepcion */
        enviarButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
                try {
                    JuegoColoresSwing.this.verificarRespuesta();
                } catch (RespuestaInvalidaException ex) {
                    JOptionPane.showMessageDialog(JuegoColoresSwing.this.frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        /*se pone el KeyAdapter para verificar si , en lugar de presionar la tecla con el mouse, se hace uso
        del enter  */
        this.respuestaField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { /*Si presiono el enter seria lo mismo que
                enviarlo con el mouse, por lo que debo verificar la respuesta*/
                    try {
                        JuegoColoresSwing.this.verificarRespuesta();
                    } catch (RespuestaInvalidaException ex) {
                        JOptionPane.showMessageDialog(JuegoColoresSwing.this.frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        //agrego los paneles al marco
        this.panelColores.add(this.colorLabel);

        this.panelRespuestas.add(new JLabel("Escribe el color:"));
        this.panelRespuestas.add(this.respuestaField);
        this.panelRespuestas.add(enviarButton);

        this.panelStats.add(this.vidasLabel);
        this.panelStats.add(this.puntosLabel);

        this.panelTiempo.add(this.tiempoLabel);

        this.panelBase.add(panelColores);
        this.panelBase.add(panelRespuestas);
        this.panelBase.add(this.panelStats);
        this.panelBase.add(this.panelTiempo);
        this.frame.add(this.panelBase);

        this.generarNuevoColor();
        //El timer se pone para que avanze cada un (1) segundo
        this.timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JuegoColoresSwing.this.tiempoLimite--;
                JuegoColoresSwing.this.tiempoLabel.setText("Tiempo: " + JuegoColoresSwing.this.tiempoLimite + "s");
                if (JuegoColoresSwing.this.tiempoLimite<=5){ //esto pone el timer de color rojo de los 5 segundos hacia abajo
                    JuegoColoresSwing.this.tiempoLabel.setForeground(Color.RED);
                }
                else{
                    JuegoColoresSwing.this.tiempoLabel.setForeground(Color.BLACK);
                }
                if (JuegoColoresSwing.this.tiempoLimite <= 0) { //si el tiempo finaliza se muestra la puntuacion final
                    JuegoColoresSwing.this.timer.stop();
                    JuegoColoresSwing.this.mostrarPuntuacionFinal();
                }

            }
        });
        this.timer.start();
        this.frame.setVisible(true);
    }


    //Se usa para generar nuevos colores al azar
    private void generarNuevoColor() {
        Random random = new Random(); //se usa para elegir un color de forma aleatoria
        /*en las siguientes lineas se elige un nombre de color y un codigo de color
        ambos al azar*/
        int indiceNombre = random.nextInt(this.colores.getNombres().size());
        int indiceCodigo = random.nextInt(this.colores.getCodigos().size());
        /*Luego se aplican los valores elegidos al color que se mostrara en pantalla
        en el momento*/
        this.colorActual = (String) this.colores.getCodigos().get(indiceCodigo);
        this.colorLabel.setText((String) this.colores.getNombres().get(indiceNombre));
        this.colorLabel.setForeground(Color.decode(this.colorActual));
    }


    /*Este metodo verifica las respuestas ingresadas por el usuario. En caso de que estas
    sean incorrectas, lanza una excepcion por lo que hace uso de manejo de excepciones*/
    private void verificarRespuesta() throws RespuestaInvalidaException {
        String respuesta = this.respuestaField.getText().trim().toUpperCase();
        this.respuestaField.setText("");
        if (respuesta.isEmpty()) { //en caso que no se escriba nada
            throw new RespuestaInvalidaException("Respuesta vacía. Intenta de nuevo.");
        } else {
            if (respuesta.equals(this.getNombreColorPorCodigo(this.colorActual).toUpperCase())) { //Si la respuesta es correcta
                this.puntos += 100;
                this.puntosLabel.setText("       Puntos: " + this.puntos);
                this.tiempoLimite += 2;
                this.generarNuevoColor();
            } else {
                this.vidas--;
                this.puntos -= 50;
                this.tiempoLimite -= 3;
                if (this.tiempoLimite <= 0) {
                    this.tiempoLimite = 1;
                }
                this.vidasLabel.setText("Vidas: " + this.vidas);
                this.puntosLabel.setText("       Puntos: " + this.puntos);
                JOptionPane.showMessageDialog(this.frame, "Incorrecto. ¡El color era " + this.getNombreColorPorCodigo(this.colorActual) + "!");
                if (this.vidas == 0) {
                    JOptionPane.showMessageDialog(this.frame, "¡Has perdido todas tus vidas!");
                    System.exit(0);
                }
            }

        }
    }


    private String getNombreColorPorCodigo(String codigoColor) {
        String devolver;
        int indice = this.colores.getCodigos().indexOf(codigoColor); //devuelve el index del del codigo o -1 si no lo encuentra
        if (indice != -1) {
            devolver = (String)this.colores.getNombres().get(indice);
        }
        else{
            devolver="";
        }
        return devolver;
    }


    private void mostrarPuntuacionFinal() {
        JOptionPane.showMessageDialog(this.frame, "¡Se acabó el tiempo! Tu puntuación final es: " + this.puntos);
        if(this.buscador.isPertenece()){ //si el jugador ya habia jugado antes
            this.buscador.actualizarPuntajes(this.usuario, Integer.toString(this.puntos));
        }
        else{ //si el jugador era nuevo
            this.buscador.escribirNuevoContenido(Integer.toString(this.puntos));
        }

        System.exit(0);
    }
}