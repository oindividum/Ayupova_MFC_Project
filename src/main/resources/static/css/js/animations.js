// // animations.js
// document.addEventListener("DOMContentLoaded", () => {
//     // Анимация кнопок
//     document.querySelectorAll(".btn").forEach(btn => {
//         btn.addEventListener("mouseenter", () => {
//             btn.style.transform = "translateY(-2px)";
//             btn.style.boxShadow = "0 4px 12px rgba(0, 86, 179, 0.3)";
//         });
//         btn.addEventListener("mouseleave", () => {
//             btn.style.transform = "translateY(0)";
//             btn.style.boxShadow = "none";
//         });
//     });
//
//     // Плавное появление контейнеров
//     document.querySelectorAll(".container, .success-container, .login-container").forEach(el => {
//         el.style.opacity = 0;
//         el.style.transform = "translateY(20px)";
//         el.style.transition = "opacity 0.6s ease, transform 0.6s ease";
//         setTimeout(() => {
//             el.style.opacity = 1;
//             el.style.transform = "translateY(0)";
//         }, 100);
//     });
//
//     document.addEventListener("DOMContentLoaded", () => {
//         const addAppointmentBtn = document.getElementById("addAppointmentBtn");
//         const appointmentForm = document.getElementById("appointmentForm");
//         const cancelBtn = document.getElementById("cancelBtn");
//
//         if (addAppointmentBtn && appointmentForm) {
//             addAppointmentBtn.addEventListener("click", () => {
//                 appointmentForm.classList.toggle("hidden"); // ✅ Показываем/скрываем форму
//                 appointmentForm.classList.toggle("fade-in"); // ✅ Добавляем анимацию появления
//             });
//         }
//
//         // if (cancelBtn) {
//         //     cancelBtn.addEventListener("click", () => {
//         //         appointmentForm.classList.add("hidden"); // ✅ Скрываем форму при отмене
//         //     });
//         // }
//     });
//
//
// });
document.addEventListener("DOMContentLoaded", () => {
    // 🔹 Анимация кнопок
    document.querySelectorAll(".btn").forEach(btn => {
        btn.addEventListener("mouseenter", () => {
            btn.style.transform = "translateY(-2px)";
            btn.style.boxShadow = "0 4px 12px rgba(0, 86, 179, 0.3)";
        });
        btn.addEventListener("mouseleave", () => {
            btn.style.transform = "translateY(0)";
            btn.style.boxShadow = "none";
        });
    });

    // 🔹 Плавное появление контейнеров
    document.querySelectorAll(".container, .success-container, .login-container").forEach(el => {
        el.style.opacity = 0;
        el.style.transform = "translateY(20px)";
        el.style.transition = "opacity 0.6s ease, transform 0.6s ease";
        setTimeout(() => {
            el.style.opacity = 1;
            el.style.transform = "translateY(0)";
        }, 100);
    });
});
