
#include <graphics.h>

int main() {

  // buka window 640x480
  initwindow(640, 480, "Demo setcolor vs setfillstyle");

  // set garis warna merah
  setcolor(RED);
  // lukis rectangle outline
  rectangle(50, 50, 200, 150);

  // set fill style corak penuh (solid) warna biru
  setfillstyle(SOLID_FILL, BLUE);
  // isi rectangle (akan isi dengan biru, outline tetap merah)
  floodfill(55, 55, RED);

  // set fill style corak silang (crosshatch) warna hijau
  setfillstyle(CROSS_FILL, GREEN);
  // bar() akan langsung isi tanpa perlu floodfill
  bar(250, 50, 400, 150);

  // setcolor tidak ubah fill, hanya outline
  setcolor(YELLOW);
  circle(150, 250, 50); // outline kuning
  // isi lingkaran dengan fillstyle sekarang (hijau cross)
  floodfill(150, 250, YELLOW);

  getch(); // tunggu input sebelum tutup
  closegraph();
  return 0;
}
