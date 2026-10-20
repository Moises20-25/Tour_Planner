package com.planes_tours.Tour_Planner.util;

public final class Alert {

    private Alert() {}

    private static String esc(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("'", "\\'")
                                 .replace("<", "\\u003c").replace("\n", " ");
    }

    public static String sweetAlertError(String text) {
        return sweetAlert("Error", text, "error");
    }

    public static String sweetAlertSuccess(String text) {
        return sweetAlert("Exitoso", text, "success");
    }

    public static String sweetAlert(String title, String msg, String icon) {
        return String.format("""
                <script>
                    Swal.fire({ title: '%s', text: '%s', icon: '%s' });
                </script>
                """, esc(title), esc(msg), esc(icon));
    }

    public static String sweetToast(String title, String icon, int timer) {
        return String.format("""
                <script>
                    Swal.mixin({
                        toast: true, position: 'top-end', showConfirmButton: false,
                        timer: %d, timerProgressBar: true
                    }).fire({ icon: '%s', title: '%s' });
                </script>
                """, timer, esc(icon), esc(title));
    }

    // Mensaje de bienvenida con foto (como el del profesor)
    public static String sweetImageUrl(String title, String text, String imageUrl) {
        return String.format("""
                <script>
                    Swal.fire({
                        title: '%s',
                        text: '%s',
                        imageUrl: '%s',
                        imageWidth: 250,
                        imageHeight: 250,
                        customClass: { image: 'rounded-circle' }
                    });
                </script>
                """, esc(title), esc(text), esc(imageUrl));
    }
}