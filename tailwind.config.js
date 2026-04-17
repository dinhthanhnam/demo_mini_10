/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/webapp/templates/**/*.{html,js}",
    "./*.{html,js}"
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['system-ui', 'sans-serif'],
      },
    },
  },
  plugins: [],
}