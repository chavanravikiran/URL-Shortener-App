function shortenURL() {
    const input = document.getElementById("urlInput").value;
    const errorDiv = document.getElementById("error");
    const resultDiv = document.getElementById("shortenedURL");

    if (!input) {
        alert("Please enter a URL");
        return;
    }

    resultDiv.innerHTML = '<h1>Loading...</h1>';

    fetch(`http://localhost:8080/shorten?url=${encodeURIComponent(input)}`)
        .then(response => {
            if (response.ok) return response.json();
            displayErrorsUsingStatusCode(response.status);
            resultDiv.innerHTML = "";
            throw new Error("Something went wrong");
        })
        .then(data => {
            resultDiv.innerHTML = showLink(data.result.full_short_link);
        })
        .catch(console.error);

    document.getElementById("urlInput").value = '';
}

function displayErrorsUsingStatusCode(code) {
    const errorDiv = document.getElementById("error");
    const messages = {
        400: "Please add a valid link",
        500: "Internal server error. Please try again later",
    };

    errorDiv.innerHTML = messages[code] || "Something went wrong. Please try again later";
    errorDiv.style.display = 'block';

    setTimeout(() => {
        errorDiv.innerHTML = '';
        errorDiv.style.display = 'none';
    }, 1500);
}

function showLink(url) {
    return `
    <div class="url-div fade-in">
        <p class="url">${url}</p>
        <div class="icons">
            <img src="http://clipground.com/images/copy-4.png" 
                title="Click to Copy"
                onclick="copyToClipboard('${url}')">
        </div>
    </div>`;
}

function copyToClipboard(text) {
    const textarea = document.createElement('textarea');
    textarea.value = text;
    document.getElementById('shortenedURL').appendChild(textarea);
    textarea.select();
    textarea.setSelectionRange(0, 99999);

    const successful = document.execCommand('copy');
    alert(successful ? 'Text Copied!' : 'Unable to copy!');
    textarea.remove();
}
