package servicios;

import entidades.Documento;

public class Nodo {

    private Documento documento;
    public Nodo izquierda;
    public Nodo derecha;

    public Nodo(Documento documento) {
        this.documento = documento;
    }

    public Nodo() {
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

}
