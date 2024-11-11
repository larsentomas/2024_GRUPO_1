package vista;

public class FalsoOptionPane implements IOptionPane {

        private String message;

        public FalsoOptionPane() {
            super();
        }

        @Override
        public void ShowMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

}
