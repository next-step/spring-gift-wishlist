document.addEventListener('DOMContentLoaded', function () {
  // --- 페이지 로드 시 기존 인증 정보 강제 정리 ---
  if (window.location.pathname === '/members/login' || window.location.pathname
      === '/members/register') {
    document.cookie = "Authorization=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    localStorage.removeItem('accessToken');
  }

  // --- 회원가입 처리 ---
  const registerForm = document.getElementById('register-form');
  if (registerForm) {
    registerForm.addEventListener('submit', handleAuthFormSubmit);
  }

  // --- 로그인 처리 ---
  const loginForm = document.getElementById('login-form');
  if (loginForm) {
    loginForm.addEventListener('submit', handleAuthFormSubmit);
  }

  // --- 위시리스트 추가 ---
  document.querySelectorAll('.add-to-wishlist-btn').forEach(button => {
    button.addEventListener('click', handleWishlistAction);
  });

  // --- 위시리스트 삭제 ---
  // 위시리스트 페이지는 동적으로 로드되므로, body에 이벤트 위임을 사용합니다.
  document.body.addEventListener('click', function (event) {
    if (event.target.classList.contains('delete-from-wishlist-btn')) {
      handleWishlistAction(event);
    }
  });

  // --- 상세 페이지 이동 ---
  document.querySelectorAll(".clickable-row").forEach(row => {
    row.addEventListener("click", function () {
      const id = this.dataset.id;
      const link = this.dataset.link;
      if (id && link) {
        window.location.href = link;
      }
    });
  });

  // --- 삭제 확인 ---
  document.querySelectorAll(".delete-form").forEach(form => {
    form.addEventListener("click", function (event) {
      event.stopPropagation();
    });

    form.addEventListener("submit", function (event) {
      if (!confirm("정말 삭제하시겠습니까?")) {
        event.preventDefault();
      }
    });
  });
});

// 로그인/회원가입 폼 제출을 위한 비동기 함수 (개선된 버전)
async function handleAuthFormSubmit(event) {
  event.preventDefault(); // 폼의 기본 제출 동작을 막습니다.
  const form = event.target;
  const url = form.action;
  const email = form.email.value;
  const password = form.password.value;

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({email, password})
    });

    const result = await response.json();

    if (response.ok) {
      // 성공 시 토큰을 저장하고 명확하게 페이지를 이동시킵니다.
      localStorage.setItem('accessToken', result.token);
      const successMsg = url.includes('register') ? '회원가입 성공!' : '로그인 성공!';
      alert(successMsg);
      window.location.href = '/members/products'; // 상품 목록 페이지로 리다이렉트
    } else {
      // 실패 시 서버가 보낸 에러 메시지를 표시합니다.
      const errorMsg = result.message || '알 수 없는 오류가 발생했습니다.';
      alert(`${url.includes('register') ? '회원가입' : '로그인'} 실패: ${errorMsg}`);
    }
  } catch (error) {
    console.error('Error during auth request:', error);
    alert('요청 중 오류가 발생했습니다. 서버가 실행 중인지 확인해주세요.');
  }
}

// 위시리스트 추가/삭제 함수 (수정 없음)
async function handleWishlistAction(event) {
  event.stopPropagation();
  const button = event.target;
  const productId = button.dataset.productId;
  const wishId = button.dataset.wishId;
  const token = localStorage.getItem('accessToken');
  const url = (productId) ? '/api/wishes' : `/api/wishes/${wishId}`;
  const method = (productId) ? 'POST' : 'DELETE';

  if (!token) {
    alert('로그인이 필요합니다.');
    window.location.href = '/members/login';
    return;
  }

  if (method === 'DELETE' && !confirm('정말 삭제하시겠습니까?')) {
    return;
  }

  try {
    const response = await fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: (productId) ? JSON.stringify({productId: productId}) : null
    });

    if (response.ok) {
      const successMsg = (method === 'POST') ? '위시리스트에 상품을 추가했습니다!'
          : '위시리스트에서 상품을 삭제했습니다.';
      alert(successMsg);
      if (method === 'DELETE') {
        window.location.reload();
      }
    } else {
      const error = await response.json();
      alert(`요청 실패: ${error.message}`);
    }
  } catch (error) {
    console.error('Error:', error);
    alert('요청 중 오류가 발생했습니다.');
  }
}