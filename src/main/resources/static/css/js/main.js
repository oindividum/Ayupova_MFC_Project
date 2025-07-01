// // Анимация кнопок
// document.querySelectorAll(".btn").forEach(btn => {
//     btn.addEventListener("mouseenter", () => {
//         btn.style.transform = "translateY(-2px)";
//         btn.style.boxShadow = "0 4px 12px rgba(0, 86, 179, 0.3)";
//     });
//     btn.addEventListener("mouseleave", () => {
//         btn.style.transform = "translateY(0)";
//         btn.style.boxShadow = "none";
//     });
// });
//
// // Плавное появление контейнеров
// document.querySelectorAll(".container, .success-container, .login-container").forEach(el => {
//     el.style.opacity = 0;
//     el.style.transform = "translateY(20px)";
//     el.style.transition = "opacity 0.6s ease, transform 0.6s ease";
//     setTimeout(() => {
//         el.style.opacity = 1;
//         el.style.transform = "translateY(0)";
//     }, 100);
// });
//
// // Открытие/закрытие модального окна записи
// const addAppointmentBtn = document.getElementById("addAppointmentBtn");
// const appointmentModal = document.getElementById("appointmentModal");
// const closeModal = document.getElementById("closeModal");
// const cancelBtn = document.getElementById("cancelBtn");
//
// if (addAppointmentBtn && appointmentModal) {
//     addAppointmentBtn.addEventListener("click", () => {
//         appointmentModal.classList.remove("hidden"); // ✅ Показываем форму
//     });
// }
//
// if (closeModal) {
//     closeModal.addEventListener("click", () => {
//         appointmentModal.classList.add("hidden"); // ✅ Закрываем форму
//     });
// }
//
// if (cancelBtn) {
//     cancelBtn.addEventListener("click", () => {
//         appointmentModal.classList.add("hidden"); // ✅ Закрываем форму
//     });
// }
//
// // ✅ Отправка формы без `event.preventDefault();`
// // document.querySelector("form").addEventListener("submit", () => {
// //     setTimeout(() => {
// //         window.location.href = "/mfc/appointments"; // ✅ Редирект спустя 2 сек
// //     }, 2000);
// // });
//
//
// document.querySelector("form").addEventListener("submit", (event) => {
//     event.preventDefault(); // ❌ Блокирует отправку!
//     alert("✅ Пациент успешно записан!");
//     window.location.href = "/user/dashboard"; // ❌ Сразу редирект!
// });
//
//
// // document.querySelector("form").addEventListener("submit", () => {
// //     setTimeout(() => {
// //         window.location.href = "/user/dashboard"; // ✅ Переносим редирект, чтобы сначала отправить данные!
// //     }, 2000);
// // });
//
//
