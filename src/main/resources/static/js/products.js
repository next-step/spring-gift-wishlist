function openUpdateModal(id, name, price, imageUrl) {
    document.getElementById('update-id').value = id;
    document.getElementById('update-name').value = name;
    document.getElementById('update-price').value = price;
    document.getElementById('update-image-url').value = imageUrl;
    document.getElementById('updateModal').style.display = 'block';
    document.getElementById('updateErrorMessage').style.display = 'none';
}

function closeModal() {
    document.querySelectorAll('.modal').forEach(modal => {
        modal.style.display = 'none';
    });
    document.getElementById('createErrorMessage').style.display = 'none';
    document.getElementById('updateErrorMessage').style.display = 'none';
}

document.addEventListener('DOMContentLoaded', function() {
  const body = document.body;
  const showCreateModal = body.dataset.showCreateModal === 'true';
  const showUpdateModal = body.dataset.showUpdateModal === 'true';

  if (showCreateModal) {
    document.getElementById('createModal').style.display = 'block';
  }
  if (showUpdateModal) {
    document.getElementById('updateModal').style.display = 'block';
  }
});

document.querySelector('.create-btn').addEventListener('click', function () {
  document.getElementById('createModal').style.display = 'block';
});

document.querySelectorAll('.update-btn').forEach(btn => {
  btn.addEventListener('click', function () {
    const id = this.dataset.id;
    const name = this.dataset.name;
    const price = this.dataset.price;
    const imageUrl = this.dataset.imageUrl;
    openUpdateModal(id,name,price,imageUrl);
  });
});

// // 추가한 부분
// function showCreateError(message) {
//     const errorElement = document.getElementById('createErrorMessage');
//     errorElement.innerHTML = message.replace(/\n/g, '<br>');
//     errorElement.style.display = 'block';
// }
//
// function showUpdateError(message) {
//     const errorElement = document.getElementById('updateErrorMessage');
//     errorElement.innerHTML = message.replace(/\n/g, '<br>');
//     errorElement.style.display = 'block';
// }
//
// document.querySelector('.create-btn').addEventListener('click', function () {
//     document.getElementById('createErrorMessage').style.display = 'none';
//     document.getElementById('createModal').style.display = 'block';
// })
//
// // 추가한 부분. 상품 추가 폼 AJAX 처리
// document.getElementById('createForm').addEventListener('submit', function(e) {
//     e.preventDefault();
//
//     const formData = {
//         name: document.getElementById('create-name').value,
//         price: document.getElementById('create-price').value,
//         imageUrl: document.getElementById('create-image-url').value
//     };
//
//     fetch('/api/products', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(formData)
//     })
//         .then(response => {
//             if (response.ok) {
//                 // 성공 시 페이지 새로고침
//                 window.location.reload();
//             } else {
//                 return response.text().then(errorMessage => {
//                     showCreateError(errorMessage || '상품 추가 중 오류가 발생했습니다.');
//                 });
//             }
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             showCreateError('상품 추가 중 오류가 발생했습니다.');
//         });
// });
//
// document.querySelectorAll('.update-btn').forEach(btn => {
//     btn.addEventListener('click', function () {
//         const id = this.dataset.id;
//         const name = this.dataset.name;
//         const price = this.dataset.price;
//         const imageUrl = this.dataset.imageUrl;
//
//         openUpdateModal(id, name, price, imageUrl);
//     });
// });
//
// document.getElementById('updateForm').addEventListener('submit', function(e) {
//     e.preventDefault();
//
//     const id = document.getElementById('update-id').value;
//     const formData = {
//         name: document.getElementById('update-name').value,
//         price: document.getElementById('update-price').value,
//         imageUrl: document.getElementById('update-image-url').value
//     };
//
//     fetch(`/api/products/${id}`, {
//         method: 'PUT',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(formData)
//     })
//         .then(response => {
//             if (response.ok) {
//                 // 성공 시 페이지 새로고침
//                 window.location.reload();
//             } else {
//                 return response.text().then(errorMessage => {
//                     showUpdateError(errorMessage || '상품 수정 중 오류가 발생했습니다.');
//                 });
//             }
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             showUpdateError('상품 수정 중 오류가 발생했습니다.');
//         });
// });

document.querySelectorAll('.delete-btn').forEach(btn => {
    btn.addEventListener('click', function () {
        document.getElementById('delete-id').value = this.dataset.id;
        document.getElementById('deleteModal').style.display = 'block';
    });
});

document.querySelectorAll('.cancel-btn').forEach(btn => {
    btn.addEventListener('click', function () {
        closeModal();
    });
});
