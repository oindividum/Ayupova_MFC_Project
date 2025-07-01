// document.addEventListener("DOMContentLoaded", () => {
//     const addAppointmentBtn = document.getElementById("addAppointmentBtn");
//     const appointmentModal = document.getElementById("appointmentModal");
//     const closeModal = document.getElementById("closeModal");
//     const cancelBtn = document.getElementById("cancelBtn");
//
//     addAppointmentBtn.addEventListener("click", () => {
//         appointmentModal.classList.remove("hidden"); // ✅ Показываем форму
//     });
//
//     closeModal.addEventListener("click", () => {
//         appointmentModal.classList.add("hidden"); // ✅ Закрываем форму
//     });
//
//     cancelBtn.addEventListener("click", () => {
//         appointmentModal.classList.add("hidden"); // ✅ Закрываем форму
//     });
//     // document.querySelector("form").addEventListener("submit", (event) => {
//     //     event.preventDefault(); // ❌ Эта строка блокирует отправку формы!
//     //     alert("✅ Пациент успешно записан к врачу!");
//     //     window.location.href = "/mfc/appointments";
//     // });
//
//     document.querySelector("form").addEventListener("submit", (event) => {
//         event.preventDefault(); // ❌ Эта строка блокирует отправку формы!
//         alert("✅ Пациент успешно записан!");
//         window.location.href = "/mfc/appointments";
//     });
//
// });
document.addEventListener("DOMContentLoaded", () => {
    const addAppointmentBtn = document.getElementById("addAppointmentBtn");
    const appointmentModal = document.getElementById("appointmentModal");
    const closeModal = document.getElementById("closeModal");
    const cancelBtn = document.getElementById("cancelBtn");

    if (addAppointmentBtn && appointmentModal) {
        addAppointmentBtn.addEventListener("click", () => {
            console.log("✅ Кнопка 'Добавить новую запись' нажата!");
            appointmentModal.classList.remove("hidden"); // ✅ Показываем форму
        });
    }

    if (closeModal) {
        closeModal.addEventListener("click", () => {
            console.log("✅ Кнопка '×' нажата!");
            appointmentModal.classList.add("hidden"); // ✅ Закрываем форму
        });
    }

    if (cancelBtn) {
        cancelBtn.addEventListener("click", () => {
            console.log("✅ Кнопка 'Отмена' нажата!");
            appointmentModal.classList.add("hidden"); // ✅ Закрываем форму
        });
    }
});
