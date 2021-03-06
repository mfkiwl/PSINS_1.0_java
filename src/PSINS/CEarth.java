package PSINS;

import static PSINS.PSINS.glv;
import static java.lang.Math.sqrt;

public class CEarth {
    double a, b;
    double f, e, e2;
    double wie;

    double sl, sl2, sl4, cl, tl, RMh, RNh, clRNh, f_RMh, f_RNh, f_clRNh;
    CVect3 pos, vn, wnie, wnen, wnin, gn, gcc;

    @Override
    public CEarth clone() {
        CEarth ret = new CEarth();

        ret.a = a;
        ret.b = b;
        ret.f = f;
        ret.e = e;
        ret.e2 = e2;
        ret.wie = wie;

        ret.sl = sl;
        ret.sl2 = sl2;
        ret.sl4 = sl4;
        ret.cl = cl;
        ret.tl = tl;
        ret.RMh = RMh;
        ret.RNh = RNh;
        ret.clRNh = clRNh;
        ret.f_RMh = f_RMh;
        ret.f_RNh = f_RNh;
        ret.f_clRNh = f_clRNh;

        ret.pos = pos.clone();
        ret.vn = vn.clone();
        ret.wnie = wnie.clone();
        ret.wnen = wnen.clone();
        ret.wnin = wnin.clone();
        ret.gn = gn.clone();
        ret.gcc = gcc.clone();

        return ret;
    }

    CEarth() {
        this(glv.Re);
    }

    CEarth(double a0) {
        this(a0, glv.f);
    }

    CEarth(double a0, double f0) {
        this(a0, f0, glv.g0);
    }

    CEarth(double a0, double f0, double g0) {
        a = a0;
        f = f0;
        wie = glv.wie;
        b = (1 - f) * a;
        e = sqrt(a * a - b * b) / a;
        e2 = e * e;
        gn = new CVect3(0, 0, -g0);
    }

    void Update(final CVect3 pos) {
        Update(pos, new CVect3(0.0));
    }

    void Update(final CVect3 pos, final CVect3 vn) {
        this.pos = pos.clone();
        this.vn = vn.clone();
        sl = Math.sin(pos.i);
        cl = Math.cos(pos.i);
        tl = sl / cl;
        double sq = 1 - e2 * sl * sl, sq2 = sqrt(sq);
        RMh = a * (1 - e2) / sq / sq2 + pos.k;
        f_RMh = 1.0 / RMh;
        RNh = a / sq2 + pos.k;
        clRNh = cl * RNh;
        f_RNh = 1.0 / RNh;
        f_clRNh = 1.0 / clRNh;
        wnie.i = 0;
        wnie.j = wie * cl;
        wnie.k = wie * sl;
        wnen.i = -vn.j * f_RMh;
        wnen.j = vn.i * f_RNh;
        wnen.k = wnen.j * tl;
        wnin = wnie.add(wnen);
        sl2 = sl * sl;
        sl4 = sl2 * sl2;
        gn.k = -(glv.g0 * (1 + 5.27094e-3 * sl2 + 2.32718e-5 * sl4) - 3.086e-6 * pos.k);
        gcc = gn.sub(wnie.add(wnin).multi(vn));
    }

    public CVect3 vn2dpos(final CVect3 vn) {
        return vn2dpos(vn, 1.0);
    }

    public CVect3 vn2dpos(final CVect3 vn, double ts) {
        return new CVect3(vn.j * f_RMh, vn.i * f_clRNh, vn.k).multi(ts);
    }
}