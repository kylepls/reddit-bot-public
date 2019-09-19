const css = `
div {
    background-color: #262628 !important;
    border-radius: 0 !important;
}
`;
const style = document.createElement('style');
document.head.appendChild(style);
style.type = 'text/css';
style.appendChild(document.createTextNode(css));
document.body.appendChild(style);

document.querySelector('#USER_DROPDOWN_ID').click()

const button = document.querySelector('button button');
if (button.getAttribute('aria-checked') !== "true") {
    button.click();
}
